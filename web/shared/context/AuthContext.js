import { createContext, useContext, useEffect, useMemo, useState, useRef } from 'react';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

let inMemoryToken = null;

export function setAccessToken(token) {
  inMemoryToken = token;
}

export function getAccessToken() {
  return inMemoryToken;
}

function getPortalUrl(role) {
  const normalizedRole = typeof role === 'string' ? role.toUpperCase() : role;
  switch (normalizedRole) {
    case 'LECTURER':
      return process.env.NEXT_PUBLIC_LECTURER_PORTAL_URL || 'http://localhost:3004';
    case 'UNIVERSITY_ADMIN':
      return process.env.NEXT_PUBLIC_UNIVERSITY_PORTAL_URL || 'http://localhost:3005';
    case 'PLATFORM_ADMIN':
      return process.env.NEXT_PUBLIC_ADMIN_PORTAL_URL || 'http://localhost:3006';
    default:
      return null;
  }
}

const AuthContext = createContext({
  openLogin: null,
  openSignup: null,
  user: null,
  accessToken: null,
  tokenType: null,
  expiresInSeconds: null,
  login: () => { },
  logout: () => { },
  goToPortal: () => { }
});

export function AuthProvider({ children, value }) {
  const [auth] = useState(null);
  const [user, setUser] = useState(null);
  const accessToken = auth?.accessToken ?? null;
  const tokenType = auth?.tokenType ?? null;
  const expiresInSeconds = auth?.expiresInSeconds ?? null;
  const didRestore = useRef(false);
  const [loading, setLoading] = useState(true);


  useEffect(() => {
    if (didRestore.current) return;
    didRestore.current = true;

    fetch(`${API_BASE}/auth/refresh`, {
      method: 'POST',
      credentials: 'include',
    })
      .then(async (res) => {
        if (!res.ok) return;
        const data = await res.json();
        setAccessToken(data.accessToken);
        setUser(data.user);
      })
      .catch((err) => {
        console.warn('Session restore failed', err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);


  const login = (payload) => {
    setAccessToken(payload.accessToken);
    setUser(payload.user);
  };

  const logout = async () => {
    try {
      await fetch(`${API_BASE}/auth/logout`, {
        method: 'POST',
        credentials: 'include',
      });
    } catch (err) {
      console.warn('Logout request failed, clearing local session anyway', err);
    }
    setAccessToken(null);
    setUser(null);
  };

  const goToPortal = (role = user?.role) => {
    if (typeof window === 'undefined') return;
    const url = getPortalUrl(role);
    if (!url) return;
    window.location.assign(url);
  };

  const contextValue = useMemo(
    () => ({
      ...(value || {}),
      user,
      accessToken,
      tokenType,
      expiresInSeconds,
      login,
      logout,
      goToPortal
    }),
    [value, user, accessToken, tokenType, expiresInSeconds]
  );

  return <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
