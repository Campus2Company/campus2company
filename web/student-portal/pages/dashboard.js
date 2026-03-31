import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import DashboardStats from '../components/DashboardStats';

const stats = [
  { icon: '📋', value: 6, label: 'Active Listings' },
  { icon: '🎓', value: '1,240', label: 'Students' },
  { icon: '🏢', value: 185, label: 'Businesses' },
  { icon: '⏳', value: 2, label: 'Pending Inquiries' },
];

const listings = [
  { title: 'AI-Powered Customer Service Platform', category: 'Technology', duration: '3 months', status: 'Active' },
  { title: 'Sustainable Supply Chain Analysis', category: 'Business', duration: '4 months', status: 'Active' },
];

export default function Dashboard() {
  return (
    <>
      <NavBar />
      <section className="dashboard-section container">
        <h1 className="dashboard-title">Dashboard</h1>
        <p className="dashboard-subtitle">Manage your projects and collaborations</p>
        <DashboardStats stats={stats} />
        <div className="dashboard-tabs">
          <button className="dashboard-tab active">My Listings</button>
          <button className="dashboard-tab">Inquiries</button>
          <button className="dashboard-tab">Notifications</button>
        </div>
        <div className="dashboard-table-section">
          <div className="dashboard-table-header">
            <span className="dashboard-table-title">Manage Your Listings</span>
            <button className="btn-primary">+ Add Listing</button>
          </div>
          <table className="dashboard-table">
            <thead>
              <tr>
                <th>Project Title</th>
                <th>Category</th>
                <th>Duration</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {listings.map((l, i) => (
                <tr key={i}>
                  <td>{l.title}</td>
                  <td>{l.category}</td>
                  <td>{l.duration}</td>
                  <td><span className="dashboard-status-active">{l.status}</span></td>
                  <td>
                    <button className="btn-table edit">Edit</button>
                    <button className="btn-table delete">Delete</button>
                    <button className="btn-table assign">Assign</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <Footer />
    </>
  );
}
