import '../../shared/styles/global.css';
import React, { useEffect, useState } from 'react';
import AuthModal from '../../shared/components/AuthModal';
import { AuthProvider } from '../../shared/context/AuthContext';

function MyApp({ Component, pageProps }) {
  const [loginOpen, setLoginOpen] = useState(false);
  const [signupOpen, setSignupOpen] = useState(false);

  const openLogin = () => setLoginOpen(true);
  const openSignup = () => setSignupOpen(true);
  const closeAll = () => {
    setLoginOpen(false);
    setSignupOpen(false);
  };

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
  }, []);

  return (
    <AuthProvider value={{ openLogin, openSignup }}>
      <Component {...pageProps} />
      <AuthModal type="login" isOpen={loginOpen} onClose={closeAll} />
      <AuthModal type="signup" isOpen={signupOpen} onClose={closeAll} />
    </AuthProvider>
  );
}

export default MyApp;
