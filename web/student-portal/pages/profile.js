import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import { useState, useEffect } from 'react';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

const STUDY_LEVEL_LABELS = {
  FIRST_YEAR: '1st Year',
  SECOND_YEAR: '2nd Year',
  THIRD_YEAR: '3rd Year',
  FOURTH_YEAR: '4th Year',
  POSTGRADUATE: 'Postgraduate',
};

const FYP_STATUS_LABELS = {
  NOT_STARTED: 'Not Started',
  IN_PROGRESS: 'In Progress',
  COMPLETED: 'Completed',
};

export default function StudentProfile() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchProfile() {
      try {
        const res = await apiFetch(`${API_BASE}/students/me`);
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
        <h2>My Profile</h2>

        {loading && <p>Loading profile...</p>}
        {error && <p style={{ color: '#ef4444' }}>{error}</p>}

        {!loading && !error && profile && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <div style={{ padding: '1.5rem', background: '#f9fafb', borderRadius: '8px', border: '1px solid #e5e7eb' }}>
              <h3 style={{ marginBottom: '1rem' }}>Personal Information</h3>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem' }}>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>First Name</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.firstName || '—'}</p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Last Name</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.lastName || '—'}</p>
                </div>
                <div style={{ gridColumn: '1 / -1' }}>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Bio</span>
                  <p style={{ margin: '0.25rem 0 0' }}>{profile.bio || '—'}</p>
                </div>
              </div>
            </div>

            <div style={{ padding: '1.5rem', background: '#f9fafb', borderRadius: '8px', border: '1px solid #e5e7eb' }}>
              <h3 style={{ marginBottom: '1rem' }}>Academic Details</h3>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem' }}>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Course</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.course || '—'}</p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Faculty</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{profile.faculty || '—'}</p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>Study Level</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{STUDY_LEVEL_LABELS[profile.studyLevel] || profile.studyLevel || '—'}</p>
                </div>
                <div>
                  <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>FYP Status</span>
                  <p style={{ margin: '0.25rem 0 0', fontWeight: 500 }}>{FYP_STATUS_LABELS[profile.fypStatus] || profile.fypStatus || '—'}</p>
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
          <p style={{ color: '#6b7280' }}>No profile found. Please complete your registration.</p>
        )}
      </section>
      <Footer />
    </>
  );
}
