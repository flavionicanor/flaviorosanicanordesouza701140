import { useEffect, useState } from "react";
import { getArtists, type Artist } from "../services/artistsService";
import { Link, useNavigate } from "react-router-dom";
import { api } from "../services/api";

export default function Artists() {
  const [artists, setArtists] = useState<Artist[]>([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<"asc" | "desc">("asc");
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [newArtistName, setNewArtistName] = useState("");

  const navigate = useNavigate();

  const [editingArtist, setEditingArtist] = useState<{
    id: number;
    name: string;
  } | null>(null);

  const [name, setName] = useState("");

  useEffect(() => {
    loadArtists();
  }, [page, sort]);

  async function loadArtists() {
    setLoading(true);
    try {
      const response = await getArtists(search, page, sort);

      console.log(response.data);
      setArtists(response.data.content);
      setTotalPages(response.data.totalPages);
    } finally {
      setLoading(false);
    }
  }

  function handleSearch() {
    setPage(0);
    loadArtists();
  }

  async function handleUpdateArtist() {
    if (!editingArtist) return;

    await api.put(`/artists/${editingArtist.id}`, {
      name,
    });

    setEditingArtist(null);
    setName("");

    loadArtists(); // reaproveita o que você já tem
  }

  const handleCreateArtist = async () => {
    if (!newArtistName.trim()) return;

    await api.post("/artists", { name: newArtistName });

    setNewArtistName("");
    loadArtists(); // recarrega lista
  };

  if (loading) return <p className="text-center">Carregando...</p>;

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-5xl mx-auto">
        <div className="text-3xl font=bold text-blue-600 mb-6">
          Cadastro de Artista
        </div>

        <div className="mb-6 flex gap-2">
          <input
            type="text"
            placeholder="Novo artista"
            value={newArtistName}
            onChange={(e) => setNewArtistName(e.target.value)}
            className="border rounded px-3 py-2 flex-1"
          />

          <button
            onClick={handleCreateArtist}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Cadastrar
          </button>
        </div>

        <div className="text-3xl font=bold text-blue-600 mb-6">
          Listagem dos Artistas
        </div>

        {/* Filtros */}
        <div className="flex gap-4 mb-6">
          <input
            type="text"
            placeholder="Buscar por nome"
            className="flex-1 px-4 py-2 border rounded"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <select
            className="px-4 py-2  border rounded"
            value={sort}
            onChange={(e) => setSort((e.target.value as "asc") || "desc")}
          >
            <option value="asc">A-Z</option>
            <option value="desc">Z-A</option>
          </select>

          <button
            onClick={handleSearch}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Buscar
          </button>
        </div>

        {/* Listagem */}
        {editingArtist && (
          <div className="mt-6 p-4 border rounded bg-gray-50">
            <h3 className="font-semibold mb-2">Editar Artista</h3>

            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="border p-2 w-full mb-3"
            />

            <div className="flex gap-2">
              <button
                className="bg-blue-600 text-white px-4 py-2 rounded"
                onClick={handleUpdateArtist}
              >
                Salvar
              </button>

              <button
                className="bg-gray-300 px-4 py-2 rounded"
                onClick={() => setEditingArtist(null)}
              >
                Cancelar
              </button>
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {artists.map((artist) => (
            <div
              onClick={() => navigate(`/artists/${artist.id}`)}
              className="bg-white p-4 rounded shadow cursor-pointer hover:bg-gray-50"
            >
              <div className="flex justify-between items-start">
                <div>
                  <h2 className="font-semibold text-lg">{artist.name}</h2>
                  <p className="text-sm text-gray-500">
                    {artist.albumsCount} álbuns
                  </p>
                </div>

                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    setEditingArtist(artist);
                    setName(artist.name);
                  }}
                  className="text-sm text-blue-600 hover:underline"
                >
                  Editar
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Paginação */}
        <div className="flex justify-between items-center mt-6">
          <button
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
          >
            Anterior
          </button>

          <span>
            Página {page + 1} de {totalPages}
          </span>

          <button
            disabled={page + 1 >= totalPages}
            onClick={() => setPage(page + 1)}
            className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
          >
            Próxima
          </button>
        </div>
      </div>
    </div>
  );
}
