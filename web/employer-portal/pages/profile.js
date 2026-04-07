import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import { useState, useEffect } from 'react';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

export default function EmployerProfile() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchProfile() {
      try {
        const res = await apiFetch(`${API_BASE}/employers/profile`);
        if (!res.ok) throw new Error('Failed to load profile');
        const data = await res.json();
        setProfile(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchProfile();
    else setLoading(false);
  }, [user]);

  return (
    <>
      <NavBar />
      <section className="container" style={{ padding: '2rem', maxWidth: '700px', margin: '0 auto' }}>
        <h2>Company Profile</h2>

        {loading && <p>Loading profile...</p>}
        {error && <p style={{ color: '#ef4444' }}>{error}</p>}

        {!loading && !error && profile && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <div style={{ padding: '1.5rem', background: '#f9fafb', borderRadius: '8px', border: '1px solid #e5e7eb' }}>
              <h3 style={{ marginBottom: '1rem' }}>Company Information</h3>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem' }}>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Company Name</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.companyName || '—'}</p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Industry</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.industry || '—'}</p>
                </div>
                <div style={{ gridColumn: '1 / -1' }}>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Description</span>
                  <p style={{ margin: '0.25rem 0 0' }}>{profile.description || '—'}</p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Website</span>
                  <p style={{ margin: '0.25rem 0 0' }}>
                    {profile.websiteUrl ? (
                      <a href={profile.websiteUrl} target="_blank" rel="noopener noreferrer" style={{ color: '#6366f1' }}>
                        {profile.websiteUrl}
                      </a>
                    ) : '—'}
                  </p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Verified</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.verified ? 'Yes' : 'No'}</p>
                </div>
              </div>
            </div>

            <div style={{ fontSize: '0.8rem', color: '#9ca3af' }}>
              Profile created: {new Date(profile.createdAt).toLocaleDateString()}
              {profile.updatedAt && ` | Last updated: ${new Date(profile.updatedAt).toLocaleDateString()}`}
            </div>
          </div>
        )}

        {!loading && !error && !profile && (
          <p style={{ color: '#6b7280' }}>No profile found. Please complete your company registration.</p>
        )}
      </section>
      <Footer />
    </>
  );
}
