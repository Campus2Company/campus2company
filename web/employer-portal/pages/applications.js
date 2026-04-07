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

export default function EmployerApplications() {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchApplications() {
      try {
        const res = await apiFetch(`${API_BASE}/applications/employer/me?size=50`);
        if (res.ok) {
          const data = await res.json();
          setApplications(data.content || []);
        }
      } catch (err) {
        console.error('Failed to load applications:', err);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchApplications();
    else setLoading(false);
  }, [user]);

  const handleStatusUpdate = async (applicationId, status) => {
    const action = status === 'ACCEPTED' ? 'accept' : 'reject';
    if (!confirm(`Are you sure you want to ${action} this application?`)) return;

    try {
      const res = await apiFetch(`${API_BASE}/applications/${applicationId}/status`, {
        method: 'PUT',
        body: JSON.stringify({ status }),
      });
      if (!res.ok) throw new Error(`Failed to ${action} application`);

      setApplications((prev) =>
        prev.map((app) =>
          app.id === applicationId ? { ...app, status } : app
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
        <h1 className="dashboard-title">Applications</h1>
        <p className="dashboard-subtitle">Review applications to your projects</p>

        {loading && <p>Loading applications...</p>}
        {!loading && applications.length === 0 && (
          <p>No applications received yet.</p>
        )}
        {!loading && applications.length > 0 && (
          <table className="dashboard-table" style={{ marginTop: '1.5rem' }}>
            <thead>
              <tr>
                <th>Project</th>
                <th>Cover Letter</th>
                <th>Status</th>
                <th>Applied</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.projectTitle}</td>
                  <td style={{ maxWidth: '300px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {app.coverLetter || '-'}
                  </td>
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
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button className="btn-table edit" onClick={() => handleStatusUpdate(app.id, 'ACCEPTED')}>
                          Accept
                        </button>
                        <button className="btn-table delete" onClick={() => handleStatusUpdate(app.id, 'REJECTED')}>
                          Reject
                        </button>
                      </div>
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
