import { useEffect, useState } from "react";
import { getArtists, type Artist } from "../services/artistsService";
import { useNavigate } from "react-router-dom";
import { api } from "../services/api";
import ArtistModal from "../components/ArtistModal";

export default function Artists() {
  const [artists, setArtists] = useState<Artist[]>([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<"asc" | "desc">("asc");
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // Controle do modal
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Edição de artista
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

    loadArtists();
  }

  if (loading) return <p className="text-center mt-10">Carregando...</p>;

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-5xl mx-auto">
        {/* Cabeçalho com botão de novo artista */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-blue-600">
            Gerenciar Artistas
          </h1>

          <button
            onClick={() => setIsModalOpen(true)}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
          >
            + Novo Artista
          </button>
        </div>

        {/* Filtros */}
        <div className="flex gap-4 mb-6">
          <input
            type="text"
            placeholder="Buscar por nome"
            className="flex-1 px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <select
            className="px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
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

        {/* Formulário de edição (quando clica em Editar) */}
        {editingArtist && (
          <div className="mt-6 p-4 border rounded bg-white shadow-sm mb-6">
            <h3 className="font-semibold mb-3">Editar Artista</h3>

            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="border p-2 w-full mb-3 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />

            <div className="flex gap-2">
              <button
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                onClick={handleUpdateArtist}
              >
                Salvar
              </button>

              <button
                className="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400"
                onClick={() => setEditingArtist(null)}
              >
                Cancelar
              </button>
            </div>
          </div>
        )}

        {/* Listagem */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {artists.map((artist) => (
            <div
              key={artist.id}
              onClick={() => navigate(`/artists/${artist.id}`)}
              className="bg-white p-4 rounded shadow cursor-pointer hover:shadow-lg transition"
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
                  ✏️ Editar
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Mensagem quando não há artistas */}
        {artists.length === 0 && (
          <div className="text-center py-12 bg-white rounded-lg shadow-sm">
            <p className="text-gray-500 text-lg mb-4">
              Nenhum artista encontrado.
            </p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
            >
              Cadastrar primeiro artista
            </button>
          </div>
        )}

        {/* Paginação */}
        {totalPages > 1 && (
          <div className="flex justify-between items-center mt-6">
            <button
              disabled={page === 0}
              onClick={() => setPage(page - 1)}
              className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50 hover:bg-gray-400"
            >
              ← Anterior
            </button>

            <span className="text-gray-700">
              Página {page + 1} de {totalPages}
            </span>

            <button
              disabled={page + 1 >= totalPages}
              onClick={() => setPage(page + 1)}
              className="px-4 py-2 bg-gray-300 rounded disabled:opacity-50 hover:bg-gray-400"
            >
              Próxima →
            </button>
          </div>
        )}
      </div>

      {/* Modal de cadastro */}
      <ArtistModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSuccess={loadArtists}
      />
    </div>
  );
}
