import { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../context/AuthContext';

const PROJECT_PORTAL_URL = process.env.NEXT_PUBLIC_PROJECT_PORTAL_URL || 'http://localhost:3007';
const HOME_URL = process.env.NEXT_PUBLIC_HOME_URL || 'http://localhost:3000';
const STUDENT_PORTAL_URL = process.env.NEXT_PUBLIC_STUDENT_PORTAL_URL || 'http://localhost:3001';
const EMPLOYER_PORTAL_URL = process.env.NEXT_PUBLIC_EMPLOYER_PORTAL_URL || 'http://localhost:3002';
const ADMIN_PORTAL_URL = process.env.NEXT_PUBLIC_ADMIN_PORTAL_URL || 'http://localhost:3003';

export default function NavBar() {
  const [menuOpen, setMenuOpen] = useState(false);
  const router = useRouter();
  const { openLogin, openSignup, user, loading, logout } = useAuth();

  useEffect(() => {
    function onClick(e) {
      if (!e.target.closest('.nav-container')) {
        setMenuOpen(false);
      }
    }
    document.addEventListener('click', onClick);
    return () => document.removeEventListener('click', onClick);
  }, []);

  const isActive = (path) => router.pathname === path;

  const getProfileUrl = () => {
    if (!user) return null;
    switch (user.role) {
      case 'STUDENT': return `${STUDENT_PORTAL_URL}/profile`;
      case 'EMPLOYER': return `${EMPLOYER_PORTAL_URL}/profile`;
      default: return null;
    }
  };

  const getDashboardUrl = () => {
    if (!user) return null;
    switch (user.role) {
      case 'PLATFORM_ADMIN': return `${ADMIN_PORTAL_URL}/dashboard`;
      default: return null;
    }
  };

  const getChatUrl = () => {
    if (!user) return null;
    switch (user.role) {
      case 'STUDENT': return `${STUDENT_PORTAL_URL}/chat`;
      case 'EMPLOYER': return `${EMPLOYER_PORTAL_URL}/chat`;
      default: return null;
    }
  };

  const chatUrl = getChatUrl();

  const profileUrl = getProfileUrl();
  const dashboardUrl = getDashboardUrl();

  return (
    <nav className="navbar" id="navbar">
      <div className="nav-container">
        <div className="nav-logo">
          <a href={HOME_URL}>Campus2Company</a>
        </div>

        <ul className={`nav-menu ${menuOpen ? 'active' : ''}`} id="navMenu">
          <li>
            <a href={HOME_URL} className={`nav-link ${isActive('/') ? 'active' : ''}`}>
              Home
            </a>
          </li>
          <li>
            <a href={PROJECT_PORTAL_URL} className="nav-link">
              Listings
            </a>
          </li>
          {user && (
            <li>
              <a href={chatUrl}
                className={`nav-link ${isActive('/chat') ? 'active' : ''}`}
              >
                Chat
              </a>
            </li>
          )}
          {user && profileUrl && user.role !== 'PLATFORM_ADMIN' && (
            <li>
              <a href={profileUrl} className="nav-link">
                Profile
              </a>
            </li>
          )}
          {user?.role === 'PLATFORM_ADMIN' && dashboardUrl && (
            <li>
              <a href={dashboardUrl} className="nav-link">
                Dashboard
              </a>
            </li>
          )}
        </ul>

        <div className="nav-auth">
          {!loading && (
            user ? (
              <button className="btn-primary" type="button" onClick={logout}>
                Logout
              </button>
            ) : (
              <>
                <button
                  className="btn-text"
                  id="loginBtn"
                  onClick={() => {
                    if (typeof window !== 'undefined' && window.openLoginModal) {
                      window.openLoginModal();
                      return;
                    }
                    openLogin?.();
                  }}
                >
                  Login
                </button>
                <button
                  className="btn-primary"
                  id="signupBtn"
                  onClick={() => {
                    if (typeof window !== 'undefined' && window.openSignupModal) {
                      window.openSignupModal();
                      return;
                    }
                    openSignup?.();
                  }}
                >
                  Sign Up
                </button>
              </>
            )
          )}
        </div>

        <button
          className="hamburger"
          id="hamburger"
          onClick={() => setMenuOpen(!menuOpen)}
        >
          <span />
          <span />
          <span />
        </button>
      </div>
    </nav>
  );
}