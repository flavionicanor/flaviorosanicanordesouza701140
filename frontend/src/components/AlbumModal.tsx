import { useState, useEffect } from "react";
import { api } from "../services/api";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  artistId: number;
  albumToEdit?: {
    id: number;
    title: string;
  } | null;
}

export default function AlbumModal({
  isOpen,
  onClose,
  onSuccess,
  artistId,
  albumToEdit,
}: Props) {
  const [title, setTitle] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (albumToEdit) {
      setTitle(albumToEdit.title);
    } else {
      setTitle("");
    }
    setError("");
  }, [albumToEdit, isOpen]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!title.trim()) {
      setError("Informe o título do álbum");
      return;
    }

    setLoading(true);

    try {
      if (albumToEdit) {
        // Editar
        await api.put(`/albums/${albumToEdit.id}`, { title: title.trim() });
      } else {
        // Criar
        await api.post("/albums", {
          title: title.trim(),
          artistId,
        });
      }

      setTitle("");
      onSuccess();
      onClose();
    } catch (err: any) {
      if (err.response?.status === 400) {
        setError("Dados inválidos");
      } else {
        setError(
          albumToEdit ? "Erro ao editar álbum" : "Erro ao criar álbum"
        );
      }
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">
            {albumToEdit ? "Editar álbum" : "Novo álbum"}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700 text-2xl"
          >
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">
              Título do álbum
            </label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Digite o título do álbum"
              autoFocus
            />
          </div>

          {error && <div className="text-red-600 text-sm">{error}</div>}

          <div className="flex gap-3 justify-end">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border rounded hover:bg-gray-50"
              disabled={loading}
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? "Salvando..." : albumToEdit ? "Salvar" : "Criar"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
