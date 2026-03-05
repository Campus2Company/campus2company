'use client'
import Link from 'next/link';
import { useState, useEffect } from 'react';
import { usePathname } from 'next/navigation';  // Replace useRouter

export default function NavBar() {
  const [menuOpen, setMenuOpen] = useState(false);
  const pathname = usePathname();  // New hook

  // close menu when clicking outside (unchanged)
  useEffect(() => {
    function onClick(e) {
      const target = e.target;
      if (!target.closest('.nav-container')) {
        setMenuOpen(false);
      }
    }
    document.addEventListener('click', onClick);
    return () => document.removeEventListener('click', onClick);
  }, []);

  const isActive = (path) => pathname === path;  // Updated

  return (
    <nav className="navbar" id="navbar">
      <div className="nav-container">
        <div className="nav-logo">
          <Link href="/">Campus2Company</Link>  {/* Removed <a> */}
        </div>
        <ul className={`nav-menu ${menuOpen ? 'active' : ''}`} id="navMenu">
          <li>
            <Link href="/" className={`nav-link ${isActive('/') ? 'active' : ''}`}>
              Home  {/* Removed <a>, moved className */}
            </Link>
          </li>
          <li>
            {/* placeholder links - prevent navigation until pages exist */}
            <a href="#" className="nav-link" onClick={e => e.preventDefault()}>
              Listings
            </a>
          </li>
          <li>
            <a href="#" className="nav-link" onClick={e => e.preventDefault()}>
              Chat
            </a>
          </li>
          <li>
            <a href="#" className="nav-link" onClick={e => e.preventDefault()}>
              Dashboard
            </a>
          </li>
          {/* remove when real pages are implemented */}
        </ul>
        <div className="nav-auth">
          {/* Buttons unchanged */}
          <button className="btn-text" id="loginBtn" onClick={() => {
            if (typeof window !== 'undefined' && window.openLoginModal) {
              window.openLoginModal();
            }
          }}>
            Login
          </button>
          <button className="btn-primary" id="signupBtn" onClick={() => {
            if (typeof window !== 'undefined' && window.openSignupModal) {
              window.openSignupModal();
            }
          }}>
            Sign Up
          </button>
        </div>
        <button className="hamburger" id="hamburger" onClick={() => setMenuOpen(!menuOpen)}>
          <span />
          <span />
          <span />
        </button>
      </div>
    </nav>
  );
}
