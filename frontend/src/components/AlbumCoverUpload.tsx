import { useState } from "react";
import { api } from "../services/api";

interface Props {
  albumId: number;
  onSuccess: () => void;
}

export default function AlbumCoverUpload({ albumId, onSuccess }: Props) {
  const [files, setFiles] = useState<FileList | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!files || files.length === 0) return;

    const formData = new FormData();

    Array.from(files).forEach((file) => {
      formData.append("files", file);
    });

    setLoading(true);

    try {
      await api.post(`/albums/${albumId}/covers`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setFiles(null);
      onSuccess();
    } finally {
      setLoading(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="mt-4 p-4 border rounded bg-gray-50"
    >
      <label className="block text-sm font-medium mb-2">Upload de capas</label>

      <input
        type="file"
        multiple
        accept="image/*"
        onChange={(e) => setFiles(e.target.files)}
        className="mb-3"
      />

      <button
        type="submit"
        disabled={loading}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
      >
        {loading ? "Enviando..." : "Enviar capas"}
      </button>
    </form>
  );
}
