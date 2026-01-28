import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../services/api";
import type { Album } from "../types/Album";
import AlbumForm from "../components/AlbumForm";
import AlbumCoverUpload from "../components/AlbumCoverUpload";

export default function ArtistDetail() {
  const { id } = useParams();
  const [albums, setAlbums] = useState<Album[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadAlbums();
  }, [id]);

  const loadAlbums = async () => {
    setLoading(true);

    await api
      .get("/albums", {
        params: { artistId: id },
      })
      .then((response) => setAlbums(response.data.content))
      .finally(() => setLoading(false));
  };

  if (loading) {
    return <p className="text-center mt-10">Carregando...</p>;
  }

  if (albums.length === 0) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <h1 className="text-2xl font-bold mb-4">Artista</h1>
        <p>Este artista ainda não possui álbuns cadastrados.</p>
      </div>
    );
  }

  const artist = albums[0].artists[0];

  return (
    <div className="max-w-4xl mx-auto p-6">
      {/* Cabeçalho */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold">{artist.name}</h1>
        <p className="text-gray-600">{albums.length} álbum(ns)</p>
      </div>

      {/* Formulário de novo álbum */}
      <div className="my-6">
        <AlbumForm artistId={Number(id)} onCreated={loadAlbums} />
      </div>

      {/* Lista de álbuns */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {albums.map((album) => (
          <div key={album.id} className="border rounded-lg p-4 shadow-sm">
            <h2 className="text-xl font-semibold mb-2">{album.title}</h2>

            {/* Capas */}
            {album.covers.length > 0 ? (
              <img
                src={album.covers[0]}
                alt={album.title}
                className="w-full h-48 object-cover rounded"
              />
            ) : (
              <div className="h-48 flex items-center justify-center bg-gray-100 rounded text-gray-500">
                Nenhum capa cadastrada
              </div>
            )}

            {/* Upload de capas */}
            <AlbumCoverUpload albumId={album.id} onSuccess={loadAlbums} />
          </div>
        ))}
      </div>
    </div>
  );
}
