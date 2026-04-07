import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiFetch } from '../api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

const TYPE_ICONS = {
  APPLICATION_SUBMITTED: '📩',
  APPLICATION_ACCEPTED: '✅',
  APPLICATION_REJECTED: '❌',
  APPLICATION_WITHDRAWN: '↩️',
  MESSAGE_RECEIVED: '💬',
  ACCOUNT_APPROVED: '🎉',
  ACCOUNT_SUSPENDED: '⚠️',
  ACCOUNT_REJECTED: '🚫',
};

export default function NotificationsPanel() {
  const { user } = useAuth();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchNotifications() {
      try {
        const res = await apiFetch(`${API_BASE}/notifications`);
        if (res.ok) {
          const data = await res.json();
          setNotifications(data);
        }
      } catch (err) {
        console.error('Failed to load notifications:', err);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchNotifications();
    else setLoading(false);
  }, [user]);

  const handleMarkAllRead = async () => {
    try {
      await apiFetch(`${API_BASE}/notifications/read-all`, { method: 'PUT' });
      setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
    } catch (err) {
      console.error('Failed to mark all as read:', err);
    }
  };

  const handleMarkRead = async (id) => {
    try {
      await apiFetch(`${API_BASE}/notifications/${id}/read`, { method: 'PUT' });
      setNotifications((prev) =>
        prev.map((n) => (n.id === id ? { ...n, read: true } : n))
      );
    } catch (err) {
      console.error('Failed to mark as read:', err);
    }
  };

  const unreadCount = notifications.filter((n) => !n.read).length;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2 style={{ margin: 0 }}>Notifications {unreadCount > 0 && <span style={{ fontSize: '0.9rem', color: '#6366f1' }}>({unreadCount} unread)</span>}</h2>
        {unreadCount > 0 && (
          <button onClick={handleMarkAllRead} style={{ background: 'none', border: 'none', color: '#6366f1', cursor: 'pointer', fontSize: '0.9rem' }}>
            Mark all as read
          </button>
        )}
      </div>

      {loading && <p>Loading notifications...</p>}
      {!loading && notifications.length === 0 && <p>No notifications yet.</p>}

      <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
        {notifications.map((n) => (
          <div
            key={n.id}
            onClick={() => !n.read && handleMarkRead(n.id)}
            style={{
              padding: '1rem',
              borderRadius: '8px',
              background: n.read ? '#f9fafb' : '#eef2ff',
              border: n.read ? '1px solid #e5e7eb' : '1px solid #c7d2fe',
              cursor: n.read ? 'default' : 'pointer',
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <span style={{ marginRight: '0.5rem' }}>{TYPE_ICONS[n.type] || '🔔'}</span>
                <strong>{n.title}</strong>
              </div>
              <span style={{ fontSize: '0.8rem', color: '#9ca3af', whiteSpace: 'nowrap' }}>
                {new Date(n.createdAt).toLocaleDateString()}
              </span>
            </div>
            <p style={{ margin: '0.25rem 0 0 1.75rem', color: '#6b7280', fontSize: '0.9rem' }}>{n.message}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
