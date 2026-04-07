import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import ChatList from '../components/ChatList';
import { useState } from 'react';

const mockChats = [
  {
    id: 1,
    avatar: 'TC',
    title: 'TechCorp Industries',
    lastMessage: 'Looking forward to your...',
    time: '2:30 PM',
    messages: [
      { from: 'them', text: 'Hello! Thank you for your interest in our AI project.', time: '2:15 PM' },
      { from: 'me', text: 'Hi! I\'m excited to work on this project. When can we discuss the details?', time: '2:20 PM' },
      { from: 'them', text: 'Looking forward to your presentation next week!', time: '2:30 PM' },
    ],
  },
  {
    id: 2,
    avatar: 'GL',
    title: 'GreenLogistics Ltd',
    lastMessage: 'Can you send over you...',
    time: '11:45 AM',
    messages: [
      { from: 'them', text: 'Can you send over your report?', time: '11:45 AM' },
    ],
  },
  {
    id: 3,
    avatar: 'SJ',
    title: 'Prof. Sarah Johnson',
    lastMessage: 'Great work on your re...',
    time: 'Yesterday',
    messages: [
      { from: 'them', text: 'Great work on your report!', time: 'Yesterday' },
      { from: 'me', text: 'Thank you, Professor!', time: 'Yesterday' },
    ],
  },
];

export default function Chat() {
  const [activeId, setActiveId] = useState(mockChats[0].id);
  const activeChat = mockChats.find(c => c.id === activeId);

  return (
    <>
      <NavBar />
      <section className="chat-section container">
        <div className="chat-layout">
          <aside className="chat-sidebar">
            <h2>Messages</h2>
            <ChatList chats={mockChats} activeId={activeId} onSelect={setActiveId} />
          </aside>
          <main className="chat-main">
            <div className="chat-header">
              <div className="chat-avatar-large">{activeChat.avatar}</div>
              <div>
                <div className="chat-title-main">{activeChat.title}</div>
                <div className="chat-status">Online</div>
              </div>
            </div>
            <div className="chat-messages">
              {activeChat.messages.map((msg, i) => (
                <div
                  key={i}
                  className={`chat-message${msg.from === 'me' ? ' me' : ''}`}
                >
                  <div className="chat-bubble">{msg.text}</div>
                  <div className="chat-time-small">{msg.time}</div>
                </div>
              ))}
            </div>
          </main>
        </div>
      </section>
      <Footer />
    </>
  );
}
