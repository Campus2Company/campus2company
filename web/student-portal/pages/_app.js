import '../styles/global.css';
import React, { useState, useEffect } from 'react';
import AuthModal from '../components/AuthModal';
import { AuthProvider } from '../context/AuthContext';

function MyApp({ Component, pageProps }) {
  const [loginOpen, setLoginOpen] = useState(false);
  const [signupOpen, setSignupOpen] = useState(false);

  const openLogin = () => {
    console.log('openLogin called');
    setLoginOpen(true);
  };
  const openSignup = () => {
    console.log('openSignup called');
    setSignupOpen(true);
  };
  const closeAll = () => {
    setLoginOpen(false);
    setSignupOpen(false);
  };

  // attach helpers to window so NavBar (and other scripts) can invoke them
  useEffect(() => {
    if (typeof window !== 'undefined') {
      window.openLoginModal = openLogin;
      window.openSignupModal = openSignup;
    }
    return () => {
      if (typeof window !== 'undefined') {
        delete window.openLoginModal;
        delete window.openSignupModal;
      }
    };
  }, [openLogin, openSignup]);

  return (
    <AuthProvider value={{ openLogin, openSignup }}>
      <Component
        {...pageProps}
        openLogin={openLogin}
        openSignup={openSignup}
      />
      <AuthModal type="login" isOpen={loginOpen} onClose={closeAll} />
      <AuthModal type="signup" isOpen={signupOpen} onClose={closeAll} />
    </AuthProvider>
  );
}

export default MyApp;
