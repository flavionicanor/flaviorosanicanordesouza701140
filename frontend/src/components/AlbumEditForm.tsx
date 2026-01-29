import { useState } from "react";
import { api } from "../services/api";

interface Props {
  albumId: number;
  currentTitle: string;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function AlbumEditForm({
  albumId,
  currentTitle,
  onCancel,
  onSuccess,
}: Props) {
  const [title, setTitle] = useState(currentTitle);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!title.trim()) return;

    setLoading(true);

    try {
      await api.put(`/albums/${albumId}`, {
        title,
      });

      onSuccess();
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mt-2 space-y-2">
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="border px-2 py-1 rounded w-full"
      />

      <div className="flex gap-2">
        <button
          type="submit"
          disabled={loading}
          className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 disabled:opacity-50"
        >
          Salvar
        </button>

        <button
          type="button"
          onClick={onCancel}
          className="bg-gray-300 px-3 py-1 rounded hover:bg-gray-400"
        >
          Cancelar
        </button>
      </div>
    </form>
  );
}
