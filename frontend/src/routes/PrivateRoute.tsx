import { Navigate } from "react-router-dom";

type Props = { children: JSX.Element };

export function PrivateRoute({ children }: Props) {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
}
