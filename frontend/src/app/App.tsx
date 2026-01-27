import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "../features/auth/LoginPage";
import Artists from "./pages/Artists";
import { PrivateRoute } from "../routes/PrivateRoute";

export function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/"
          element={
            <PrivateRoute>
              <Artists />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}
