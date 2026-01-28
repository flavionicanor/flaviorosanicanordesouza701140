import { useState } from "react";
import { api } from "../services/api";

interface AlbumFormProps {
  artistId: number;
  onCreated: () => void;
}

export default function AlbumForm({ artistId, onCreated }: AlbumFormProps) {
  const [title, setTitle] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");

    if (!title.trim()) {
      setError("Informe o título do álbum");
      return;
    }

    try {
      setLoading(true);

      await api.post("/albums", {
        title: title.trim(),
        artistId,
      });

      setTitle("");
      onCreated(); // recarrega os dados do artista
    } catch (err: any) {
      if (err.response?.status === 400) {
        setError("Dados inválidos");
      } else {
        setError("Erro ao criar álbum");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="mt-6 space-y-3">
      <h2 className="text-lg font-semibold">Adicionar álbum</h2>

      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="w-full border rounded px-3 py-2"
        placeholder="Título do álbum"
      />

      {error && <div className="text-red-600 text-sm">{error}</div>}

      <button
        type="submit"
        disabled={loading}
        className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 disabled:opacity-60"
      >
        {loading ? "Salvando..." : "Adicionar"}
      </button>
    </form>
  );
}
