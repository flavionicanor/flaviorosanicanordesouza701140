import { useEffect, useState } from "react";
import { getArtists, type Artist } from "../services/artistsService";

export default function Artists() {
  const [artists, setArtists] = useState<Artist[]>([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<"asc" | "desc">("asc");
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

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

  if (loading) return <p className="text-center">Carregando...</p>;

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-5xl mx-auto">
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
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {artists.map((artist) => (
            <div
              key={artist.id}
              className="p-4 border rounded shadow hover:shadow-lg transition"
            >
              <h2 className="font-semibold">{artist.name}</h2>
              <p className="text-sm text-gray-600">
                Álbuns: {artist.albumsCount}
              </p>
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
