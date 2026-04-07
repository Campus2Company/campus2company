import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import DashboardStats from '../components/DashboardStats';
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

export default function Dashboard() {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const res = await apiFetch(`${API_BASE}/applications/student/me?size=50`);
        if (res.ok) {
          const data = await res.json();
          setApplications(data.content || []);
        }
      } catch (err) {
        console.error('Failed to load dashboard data:', err);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchData();
    else setLoading(false);
  }, [user]);

  const pending = applications.filter((a) => a.status === 'PENDING').length;
  const accepted = applications.filter((a) => a.status === 'ACCEPTED').length;
  const rejected = applications.filter((a) => a.status === 'REJECTED').length;
  const total = applications.length;

  const stats = [
    { icon: '📝', value: total, label: 'Total Applications' },
    { icon: '⏳', value: pending, label: 'Pending' },
    { icon: '✅', value: accepted, label: 'Accepted' },
    { icon: '❌', value: rejected, label: 'Rejected' },
  ];

  return (
    <>
      <NavBar />
      <section className="dashboard-section container">
        <h1 className="dashboard-title">Student Dashboard</h1>
        <p className="dashboard-subtitle">Track your applications and progress</p>
        <DashboardStats stats={stats} />

        <div className="dashboard-table-section">
          <div className="dashboard-table-header">
            <span className="dashboard-table-title">Recent Applications</span>
          </div>
          {loading && <p>Loading...</p>}
          {!loading && applications.length === 0 && (
            <p style={{ padding: '1rem' }}>No applications yet. Browse projects to get started!</p>
          )}
          {!loading && applications.length > 0 && (
            <table className="dashboard-table">
              <thead>
                <tr>
                  <th>Project</th>
                  <th>Status</th>
                  <th>Applied</th>
                </tr>
              </thead>
              <tbody>
                {applications.slice(0, 10).map((app) => (
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
