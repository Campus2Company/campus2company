import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import NotificationsPanel from '../../shared/components/NotificationsPanel';

export default function NotificationsPage() {
  return (
    <>
      <NavBar />
      <section className="container" style={{ padding: '2rem' }}>
        <NotificationsPanel />
      </section>
      <Footer />
    </>
  );
}
