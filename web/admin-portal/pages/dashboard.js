import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import { useState, useEffect } from 'react';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

export default function AdminDashboard() {
  const { user } = useAuth();
  const [pendingEmployers, setPendingEmployers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchPending() {
      try {
        const res = await apiFetch(`${API_BASE}/auth/admin/employers/pending`);
        if (!res.ok) throw new Error('Failed to load pending employers');
        const data = await res.json();
        setPendingEmployers(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchPending();
    else setLoading(false);
  }, [user]);

  const handleAction = async (userId, action) => {
    const label = action === 'approve' ? 'approve' : 'reject';
    if (!confirm(`Are you sure you want to ${label} this employer?`)) return;

    try {
      const res = await apiFetch(`${API_BASE}/auth/admin/employers/${userId}/${action}`, {
        method: 'PUT',
      });
      if (!res.ok) throw new Error(`Failed to ${label} employer`);
      setPendingEmployers((prev) => prev.filter((e) => e.id !== userId));
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <>
      <NavBar />
      <section className="container" style={{ padding: '2rem' }}>
        <h1 className="dashboard-title">Platform Admin Dashboard</h1>
        <p className="dashboard-subtitle">Manage employer registrations and platform users</p>

        <div className="dashboard-table-section" style={{ marginTop: '2rem' }}>
          <div className="dashboard-table-header">
            <span className="dashboard-table-title">Pending Employer Approvals</span>
          </div>

          {loading && <p style={{ padding: '1rem' }}>Loading...</p>}
          {error && <p style={{ padding: '1rem', color: '#ef4444' }}>Error: {error}</p>}
          {!loading && !error && pendingEmployers.length === 0 && (
            <p style={{ padding: '1rem' }}>No pending employer registrations.</p>
          )}
          {!loading && pendingEmployers.length > 0 && (
            <table className="dashboard-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Status</th>
                  <th>Registered</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {pendingEmployers.map((emp) => (
                  <tr key={emp.id}>
                    <td>{emp.firstName} {emp.lastName}</td>
                    <td>{emp.email}</td>
                    <td>
                      <span style={{
                        padding: '0.25rem 0.75rem', borderRadius: '9999px',
                        fontSize: '0.85rem', fontWeight: '500',
                        background: '#fef3c7', color: '#92400e',
                      }}>
                        {emp.status}
                      </span>
                    </td>
                    <td>{emp.createdAt ? new Date(emp.createdAt).toLocaleDateString() : '-'}</td>
                    <td>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button className="btn-table edit" onClick={() => handleAction(emp.id, 'approve')}>
                          Approve
                        </button>
                        <button className="btn-table delete" onClick={() => handleAction(emp.id, 'reject')}>
                          Reject
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>
      <Footer />
    </>
  );
}
