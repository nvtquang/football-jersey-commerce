import { createContext, useContext, useMemo, useState } from 'react';
import { authApi } from '../../lib/tqsportApi.js';

const AuthContext = createContext(null);
const SESSION_KEY = 'tqsport_user';
const TOKEN_KEY = 'tqsport_token';

function saveSession(authResponse) {
  const nextUser = {
    email: authResponse.email,
    name: authResponse.fullName,
    role: authResponse.role,
  };
  localStorage.setItem(SESSION_KEY, JSON.stringify(nextUser));
  localStorage.setItem(TOKEN_KEY, authResponse.token);
  return nextUser;
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem(SESSION_KEY) || 'null'));

  const value = useMemo(() => ({
    user,
    async login(email, password) {
      const authResponse = await authApi.login({ email, password });
      const nextUser = saveSession(authResponse);
      setUser(nextUser);
      return nextUser;
    },
    async register({ name, email, password }) {
      const authResponse = await authApi.register({ fullName: name, email, password });
      const nextUser = saveSession(authResponse);
      setUser(nextUser);
      return nextUser;
    },
    logout() {
      localStorage.removeItem(SESSION_KEY);
      localStorage.removeItem(TOKEN_KEY);
      setUser(null);
    },
  }), [user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
