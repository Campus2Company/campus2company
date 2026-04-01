export default function Footer() {
  const PROJECT_PORTAL_URL = process.env.NEXT_PUBLIC_PROJECT_PORTAL_URL || 'http://localhost:3007';

  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section">
            <h4>Campus2Company</h4>
            <p>Project listings portal.</p>
          </div>
          <div className="footer-section">
            <h4>Quick Links</h4>
            <ul>
              <li><a href={PROJECT_PORTAL_URL}>Browse Projects</a></li>
            </ul>
          </div>
        </div>
        <div className="footer-bottom">
          <p>&copy; 2026 Campus2Company. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}
