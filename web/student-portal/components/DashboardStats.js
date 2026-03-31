export default function DashboardStats({ stats }) {
  return (
    <div className="dashboard-stats">
      {stats.map((stat, i) => (
        <div className="dashboard-stat" key={i}>
          <div className="dashboard-stat-icon">{stat.icon}</div>
          <div className="dashboard-stat-value">{stat.value}</div>
          <div className="dashboard-stat-label">{stat.label}</div>
        </div>
      ))}
    </div>
  );
}
