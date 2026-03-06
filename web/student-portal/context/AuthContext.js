import React, { createContext, useContext } from 'react';

// context holds the two modal openers
const AuthContext = createContext({
  openLogin: null,
  openSignup: null
});

export function AuthProvider({ children, value }) {
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
