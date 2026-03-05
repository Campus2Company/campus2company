import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import Link from 'next/link';

export default function Home() {
  return (
    <>
      <NavBar />

      {/* Hero Section */}
      <section className="hero">
        <div className="hero-overlay" />
        <div className="hero-content">
          <h1 className="hero-title">Connecting Universities with Industry</h1>
          <p className="hero-subtitle">Bridging talent and opportunity</p>
          {/* placeholder button - no navigation until listings page is ready */}
          <button className="btn-hero" onClick={e => e.preventDefault()}>
            Browse Opportunities
          </button>
        </div>
      </section>

      {/* Features Section */}
      <section className="features">
        <div className="container">
          <h2 className="section-title">Why Campus2Company?</h2>

          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">🎓</div>
              <h3>For Students</h3>
              <p>
                Gain real-world experience through industry projects and build your
                professional network.
              </p>
            </div>

            <div className="feature-card">
              <div className="feature-icon">🏢</div>
              <h3>For Businesses</h3>
              <p>
                Access fresh talent and innovative ideas while contributing to
                education.
              </p>
            </div>

            <div className="feature-card">
              <div className="feature-icon">👨‍🏫</div>
              <h3>For Lecturers</h3>
              <p>
                Connect your students with industry partners and enrich your
                curriculum.
              </p>
            </div>

            <div className="feature-card">
              <div className="feature-icon">🤝</div>
              <h3>Collaboration</h3>
              <p>
                Seamless communication tools to manage projects and relationships.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats">
        <div className="container">
          <div className="stats-grid">
            <div className="stat-item">
              <h3 className="stat-number">250+</h3>
              <p className="stat-label">Active Projects</p>
            </div>
            <div className="stat-item">
              <h3 className="stat-number">1,200+</h3>
              <p className="stat-label">Students</p>
            </div>
            <div className="stat-item">
              <h3 className="stat-number">180+</h3>
              <p className="stat-label">Partner Businesses</p>
            </div>
            <div className="stat-item">
              <h3 className="stat-number">95%</h3>
              <p className="stat-label">Satisfaction Rate</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta">
        <div className="container">
          <h2>Ready to Get Started?</h2>
          <p>Join thousands of students, businesses, and educators collaborating today.</p>
          <button
            className="btn-primary btn-large"
            onClick={() => {
              if (typeof window !== 'undefined' && window.openSignupModal) {
                window.openSignupModal();
              }
            }}
          >
            Create Your Account
          </button>
        </div>
      </section>

      <Footer />

      {/* Login/Signup modals could go here as components if implemented */}
    </>
  );
}
