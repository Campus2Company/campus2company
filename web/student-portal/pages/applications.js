import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import { useState, useEffect } from 'react';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

const STATUS_STYLES = {
  PENDING: { background: '#fef3c7', color: '#92400e' },
  ACCEPTED: { background: '#d1fae5', color: '#065f46' },
  REJECTED: { background: '#fee2e2', color: '#991b1b' },
  WITHDRAWN: { background: '#e5e7eb', color: '#374151' },
};

export default function ApplicationsPage() {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchApplications() {
      try {
        const res = await apiFetch(`${API_BASE}/applications/student/me?size=50`);
        if (!res.ok) throw new Error('Failed to load applications');
        const data = await res.json();
        setApplications(data.content || []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchApplications();
  }, [user]);

  const handleWithdraw = async (applicationId) => {
    if (!confirm('Are you sure you want to withdraw this application?')) return;
    try {
      const res = await apiFetch(`${API_BASE}/applications/${applicationId}/withdraw`, {
        method: 'PUT',
      });
      if (!res.ok) throw new Error('Failed to withdraw');
      setApplications((prev) =>
        prev.map((app) =>
          app.id === applicationId ? { ...app, status: 'WITHDRAWN' } : app
        )
      );
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <>
      <NavBar />
      <section className="container" style={{ padding: '2rem' }}>
        <h1 className="dashboard-title">My Applications</h1>
        <p className="dashboard-subtitle">Track your project applications</p>

        {loading && <p>Loading applications...</p>}
        {error && <p style={{ color: '#ef4444' }}>Error: {error}</p>}
        {!loading && !error && applications.length === 0 && (
          <p>You haven't applied to any projects yet.</p>
        )}

        {!loading && applications.length > 0 && (
          <table className="dashboard-table" style={{ marginTop: '1.5rem' }}>
            <thead>
              <tr>
                <th>Project</th>
                <th>Status</th>
                <th>Applied</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.projectTitle}</td>
                  <td>
                    <span
                      style={{
                        padding: '0.25rem 0.75rem',
                        borderRadius: '9999px',
                        fontSize: '0.85rem',
                        fontWeight: '500',
                        ...(STATUS_STYLES[app.status] || {}),
                      }}
                    >
                      {app.status}
                    </span>
                  </td>
                  <td>{new Date(app.appliedAt).toLocaleDateString()}</td>
                  <td>
                    {app.status === 'PENDING' && (
                      <button
                        className="btn-table delete"
                        onClick={() => handleWithdraw(app.id)}
                      >
                        Withdraw
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
      <Footer />
    </>
  );
}
