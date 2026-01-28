import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "../pages/auth/LoginPage";
import Artists from "../pages/Artists";
import { PrivateRoute } from "../routes/PrivateRoute";
import ArtistDetail from "../pages/ArtistDetail";

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

        <Route
          path="/artists/:id"
          element={
            <PrivateRoute>
              <ArtistDetail />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}
