import { useState } from "react";
import { api } from "../../services/api";

export function LoginPage() {
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    setError(null);

    if (!username || username.length < 3) {
      setError("Usuário deve ter pelo menos 3 caracteres!!!");
      return;
    }

    if (!password || password.length < 3) {
      setError("Senha deve ter pelo menos 3 caracteres!!!");
      return;
    }

    //chamar a api
    console.log("Login válido: ", { username, password });

    api
      .post("/auth/login", { username, password })
      .then((response) => {
        console.log("Token recebido: ", response.data);
        localStorage.setItem("accessToken", response.data.accessToken);
      })
      .catch(() => setError("Usuário ou senha inválidos"));
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="w-full max-w-sm bg-white p-6 rounded-lg shadow">
        <h1 className="text-2xl font-bold text-center mb-6 text-blue-600">
          Login de Acesso
        </h1>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Usuário"
            value={username}
            onChange={(e) => setUserName(e.target.value)}
            className="w-full px-3 py-2 border rounded focus:outline-none focus:ring"
          />

          <input
            type="password"
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-3 py-2 border rounded focus:outline-none focus:ring"
          />

          {error && <p className="text-red-600 text-sm text-center">{error}</p>}

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
          >
            Entrar
          </button>
        </form>
      </div>
    </div>
  );
}
