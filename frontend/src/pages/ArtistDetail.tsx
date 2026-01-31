import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../services/api";
import type { Album } from "../types/Album";
import AlbumModal from "../components/AlbumModal";
import CoverUploadModal from "../components/CoverUploadModal";

export default function ArtistDetail() {
  const { id } = useParams();
  const [albums, setAlbums] = useState<Album[]>([]);
  const [loading, setLoading] = useState(true);

  // Controle dos modais
  const [isAlbumModalOpen, setIsAlbumModalOpen] = useState(false);
  const [isCoverModalOpen, setIsCoverModalOpen] = useState(false);

  // Dados para edição
  const [albumToEdit, setAlbumToEdit] = useState<{
    id: number;
    title: string;
  } | null>(null);

  const [albumForCover, setAlbumForCover] = useState<{
    id: number;
    title: string;
  } | null>(null);

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

  const handleOpenCreateModal = () => {
    setAlbumToEdit(null);
    setIsAlbumModalOpen(true);
  };

  const handleOpenEditModal = (album: Album) => {
    setAlbumToEdit({
      id: album.id,
      title: album.title,
    });
    setIsAlbumModalOpen(true);
  };

  const handleOpenCoverModal = (album: Album) => {
    setAlbumForCover({
      id: album.id,
      title: album.title,
    });
    setIsCoverModalOpen(true);
  };

  if (loading) {
    return <p className="text-center mt-10">Carregando...</p>;
  }

  const artist = albums.length > 0 ? albums[0].artists[0] : null;

  return (
    <div className="max-w-6xl mx-auto p-6">
      {/* Cabeçalho com botão de novo álbum */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {artist ? artist.name : "Artista"}
          </h1>
          <p className="text-gray-600">{albums.length} álbum(ns)</p>
        </div>

        <button
          onClick={handleOpenCreateModal}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
        >
          + Novo álbum
        </button>
      </div>

      {/* Mensagem quando não há álbuns */}
      {albums.length === 0 && (
        <div className="text-center py-12 bg-gray-50 rounded-lg">
          <p className="text-gray-500 text-lg mb-4">
            Este artista ainda não possui álbuns cadastrados.
          </p>
          <button
            onClick={handleOpenCreateModal}
            className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
          >
            Cadastrar primeiro álbum
          </button>
        </div>
      )}

      {/* Lista de álbuns */}
      <div className="space-y-8">
        {albums.map((album) => (
          <div key={album.id} className="border rounded-lg p-6 shadow-sm">
            {/* Cabeçalho do álbum com botões de ação */}
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-semibold">{album.title}</h3>

              <div className="flex gap-2">
                <button
                  onClick={() => handleOpenEditModal(album)}
                  className="px-4 py-2 bg-gray-100 text-gray-700 rounded hover:bg-gray-200 transition"
                >
                  ✏️ Editar
                </button>

                <button
                  onClick={() => handleOpenCoverModal(album)}
                  className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
                >
                  📷 Enviar capa
                </button>
              </div>
            </div>

            {/* Grid de capas */}
            {album.covers.length > 0 ? (
              <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {album.covers.map((coverUrl, index) => (
                  <div
                    key={index}
                    className="aspect-square rounded-lg overflow-hidden shadow-md hover:shadow-lg transition"
                  >
                    <img
                      src={coverUrl}
                      alt={`${album.title} - Capa ${index + 1}`}
                      className="w-full h-full object-cover"
                    />
                  </div>
                ))}
              </div>
            ) : (
              <div className="h-48 flex items-center justify-center bg-gray-100 rounded-lg text-gray-500">
                <div className="text-center">
                  <p className="mb-2">Nenhuma capa cadastrada</p>
                  <button
                    onClick={() => handleOpenCoverModal(album)}
                    className="text-blue-600 hover:underline"
                  >
                    Clique aqui para enviar
                  </button>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Modal de cadastro/edição de álbum */}
      <AlbumModal
        isOpen={isAlbumModalOpen}
        onClose={() => setIsAlbumModalOpen(false)}
        onSuccess={loadAlbums}
        artistId={Number(id)}
        albumToEdit={albumToEdit}
      />

      {/* Modal de upload de capas */}
      {albumForCover && (
        <CoverUploadModal
          isOpen={isCoverModalOpen}
          onClose={() => setIsCoverModalOpen(false)}
          onSuccess={loadAlbums}
          albumId={albumForCover.id}
          albumTitle={albumForCover.title}
        />
      )}
    </div>
  );
}
