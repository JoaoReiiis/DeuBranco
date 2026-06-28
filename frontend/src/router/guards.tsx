import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export function AuthGuard() {
  const { token, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) return null;
  if (!token) return <Navigate to="/login" state={{ from: location }} replace />;

  return <Outlet />;
}

export function AdminGuard() {
  const { isAdmin, isLoading } = useAuth();

  if (isLoading) return null;
  if (!isAdmin) return <Navigate to="/" replace />;

  return <Outlet />;
}

export function PublicOnlyGuard() {
  const { token, isLoading } = useAuth();

  if (isLoading) return null;
  if (token) return <Navigate to="/" replace />;

  return <Outlet />;
}
