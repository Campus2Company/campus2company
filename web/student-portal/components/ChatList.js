export default function ChatList({ chats, activeId, onSelect }) {
  return (
    <div className="chat-list">
      {chats.map(chat => (
        <div
          key={chat.id}
          className={`chat-list-item${chat.id === activeId ? ' active' : ''}`}
          onClick={() => onSelect(chat.id)}
        >
          <div className="chat-avatar">{chat.avatar}</div>
          <div className="chat-info">
            <div className="chat-title">{chat.title}</div>
            <div className="chat-last">{chat.lastMessage}</div>
          </div>
          <div className="chat-time">{chat.time}</div>
        </div>
      ))}
    </div>
  );
}
