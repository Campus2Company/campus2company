import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import ChatList from '../components/ChatList';
import { useState, useEffect } from 'react';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

function getInitials(id) {
  if (!id) return '??';
  return id.substring(0, 2).toUpperCase();
}

function formatTime(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const now = new Date();
  const diffDays = Math.floor((now - date) / (1000 * 60 * 60 * 24));
  if (diffDays === 0) return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  if (diffDays === 1) return 'Yesterday';
  return date.toLocaleDateString();
}

export default function Chat() {
  const { user } = useAuth();
  const [conversations, setConversations] = useState([]);
  const [activeId, setActiveId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);

  useEffect(() => {
    async function fetchConversations() {
      try {
        const res = await apiFetch(`${API_BASE}/messages/conversations`);
        if (res.ok) {
          const data = await res.json();
          setConversations(data);
          if (data.length > 0) setActiveId(data[0].id);
        }
      } catch (err) {
        console.error('Failed to load conversations:', err);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchConversations();
    else setLoading(false);
  }, [user]);

  useEffect(() => {
    if (!activeId) return;
    async function fetchMessages() {
      try {
        const res = await apiFetch(`${API_BASE}/messages/conversations/${activeId}`);
        if (res.ok) {
          const data = await res.json();
          setMessages(data);
        }
        await apiFetch(`${API_BASE}/messages/conversations/${activeId}/read`, { method: 'PUT' });
      } catch (err) {
        console.error('Failed to load messages:', err);
      }
    }
    fetchMessages();
  }, [activeId]);

  const activeConversation = conversations.find((c) => c.id === activeId);
  const otherParticipantId = activeConversation
    ? (activeConversation.participantOneId === user?.id ? activeConversation.participantTwoId : activeConversation.participantOneId)
    : null;

  const chatListItems = conversations.map((c) => {
    const otherId = c.participantOneId === user?.id ? c.participantTwoId : c.participantOneId;
    return {
      id: c.id,
      avatar: getInitials(otherId),
      title: otherId ? `User ${otherId.substring(0, 8)}...` : 'Unknown',
      lastMessage: c.lastMessageContent ? c.lastMessageContent.substring(0, 30) + (c.lastMessageContent.length > 30 ? '...' : '') : '',
      time: formatTime(c.lastMessageAt),
    };
  });

  const handleSend = async (e) => {
    e.preventDefault();
    if (!newMessage.trim() || !activeConversation || sending) return;
    setSending(true);

    try {
      const res = await apiFetch(`${API_BASE}/messages`, {
        method: 'POST',
        body: JSON.stringify({
          conversationId: activeId,
          recipientId: otherParticipantId,
          projectId: activeConversation.projectId,
          content: newMessage.trim(),
        }),
      });
      if (res.ok) {
        const sent = await res.json();
        setMessages((prev) => [...prev, sent]);
        setNewMessage('');
      }
    } catch (err) {
      console.error('Failed to send message:', err);
    } finally {
      setSending(false);
    }
  };

  return (
    <>
      <NavBar />
      <section className="chat-section container">
        <div className="chat-layout">
          <aside className="chat-sidebar">
            <h2>Messages</h2>
            {loading && <p>Loading...</p>}
            {!loading && conversations.length === 0 && <p style={{ padding: '0.5rem', color: '#9ca3af' }}>No conversations yet.</p>}
            {!loading && conversations.length > 0 && (
              <ChatList chats={chatListItems} activeId={activeId} onSelect={setActiveId} />
            )}
          </aside>
          <main className="chat-main">
            {activeConversation ? (
              <>
                <div className="chat-header">
                  <div className="chat-avatar-large">{getInitials(otherParticipantId)}</div>
                  <div>
                    <div className="chat-title-main">
                      {otherParticipantId ? `User ${otherParticipantId.substring(0, 8)}...` : 'Unknown'}
                    </div>
                  </div>
                </div>
                <div className="chat-messages">
                  {messages.map((msg) => (
                    <div
                      key={msg.id}
                      className={`chat-message${msg.senderId === user?.id ? ' me' : ''}`}
                    >
                      <div className="chat-bubble">{msg.content}</div>
                      <div className="chat-time-small">{formatTime(msg.sentAt)}</div>
                    </div>
                  ))}
                </div>
                <form onSubmit={handleSend} style={{ display: 'flex', gap: '0.5rem', padding: '1rem', borderTop: '1px solid #e5e7eb' }}>
                  <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Type a message..."
                    style={{ flex: 1, padding: '0.75rem', borderRadius: '6px', border: '1px solid #d1d5db' }}
                  />
                  <button className="btn-primary" type="submit" disabled={sending}>
                    {sending ? '...' : 'Send'}
                  </button>
                </form>
              </>
            ) : (
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%', color: '#9ca3af' }}>
                {loading ? 'Loading...' : 'Select a conversation or start a new one'}
              </div>
            )}
          </main>
        </div>
      </section>
      <Footer />
    </>
  );
}
