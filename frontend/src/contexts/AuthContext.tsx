import {
  createContext,
  useState,
  useEffect,
  useCallback,
  type ReactNode,
} from 'react';
import authService from '../services/authService';
import { setLogoutCallback, TOKEN_KEY } from '../services/api';
import type { JogadorResponse } from '../types/auth';

interface AuthContextValue {
  token: string | null;
  user: JogadorResponse | null;
  isAdmin: boolean;
  isLoading: boolean;
  login: (email: string, senha: string) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem(TOKEN_KEY));
  const [user, setUser] = useState<JogadorResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    setToken(null);
    setUser(null);
  }, []);

  useEffect(() => {
    setLogoutCallback(logout);
  }, [logout]);

  useEffect(() => {
    if (!token) {
      setIsLoading(false);
      return;
    }

    authService
      .me()
      .then(setUser)
      .catch(() => logout())
      .finally(() => setIsLoading(false));
  }, [token, logout]);

  const login = useCallback(async (email: string, senha: string) => {
    const res = await authService.login({ email, senha });
    localStorage.setItem(TOKEN_KEY, res.accessToken);
    setToken(res.accessToken);
    const me = await authService.me();
    setUser(me);
  }, []);

  const isAdmin = user?.role === 'ADMIN';

  return (
    <AuthContext.Provider value={{ token, user, isAdmin, isLoading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
