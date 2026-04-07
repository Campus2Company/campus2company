import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import ProjectCard from '../components/ProjectCard';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

const CATEGORY_LABELS = {
  CONTENT_WRITING: 'Content Writing',
  GRAPHIC_DESIGN_MULTIMEDIA: 'Graphic Design & Multimedia',
  SOFTWARE_ENGINEERING: 'Software Engineering',
  DATA_SCIENCE_ANALYTICS: 'Data Science & Analytics',
  AI_MACHINE_LEARNING: 'AI & Machine Learning',
  CYBERSECURITY: 'Cybersecurity',
  UI_UX_DESIGN: 'UI/UX Design',
  RESEARCH: 'Research',
};

function formatCategory(cat) {
  return CATEGORY_LABELS[cat] || cat;
}

function formatDeadline(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const now = new Date();
  const diffMs = date - now;
  const diffDays = Math.ceil(diffMs / (1000 * 60 * 60 * 24));
  if (diffDays < 0) return 'Closed';
  if (diffDays <= 30) return `${diffDays} days left`;
  const months = Math.ceil(diffDays / 30);
  return `${months} month${months > 1 ? 's' : ''}`;
}

export default function ListingsPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('All Categories');
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchProjects() {
      try {
        const res = await apiFetch(`${API_BASE}/projects/all?status=OPEN&size=50`);
        if (!res.ok) throw new Error('Failed to load projects');
        const data = await res.json();
        setProjects(data.content || []);
      } catch (err) {
        console.error('Error fetching projects:', err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    fetchProjects();
  }, []);

  const filtered = projects.filter(
    (project) => {
      const cats = (project.categories || []).map(formatCategory);
      const matchesCategory = category === 'All Categories' || cats.includes(category);
      const searchLower = search.toLowerCase();
      const matchesSearch =
        project.title.toLowerCase().includes(searchLower) ||
        project.description.toLowerCase().includes(searchLower);
      return matchesCategory && matchesSearch;
    }
  );

  const allCategories = Array.from(
    new Set(projects.flatMap((p) => (p.categories || []).map(formatCategory)))
  );

  const handleViewDetails = (project) => {
    if (!user) {
      window.openLoginModal?.();
      return;
    }
    router.push(`/projects/${project.id}`);
  };

  return (
    <>
      <NavBar />
      <section className="listings-section container">
        <h1 className="listings-title">Project Opportunities</h1>
        <p className="listings-subtitle">Discover exciting projects and collaborations</p>
        <div className="listings-filters">
          <input
            type="text"
            placeholder="Search projects..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="listings-search"
          />
          <select
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            className="listings-category"
          >
            <option>All Categories</option>
            {allCategories.map((cat) => (
              <option key={cat}>{cat}</option>
            ))}
          </select>
        </div>
        {loading && <p className="listings-loading">Loading projects...</p>}
        {error && <p className="listings-error">Error: {error}</p>}
        {!loading && !error && filtered.length === 0 && (
          <p className="listings-empty">No projects found.</p>
        )}
        <div className="listings-grid">
          {filtered.map((project) => (
            <ProjectCard
              key={project.id}
              title={project.title}
              company={project.employerId}
              description={project.description}
              tags={(project.categories || []).map(formatCategory)}
              time={formatDeadline(project.applicationDeadline)}
              onViewDetails={() => handleViewDetails(project)}
            />
          ))}
        </div>
      </section>
      <Footer />
    </>
  );
}
