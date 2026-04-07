import NavBar from '../../../shared/components/NavBar';
import Footer from '../../../shared/components/Footer';
import { useRouter } from 'next/router';
import { useState, useEffect } from 'react';
import { useAuth } from '../../../shared/context/AuthContext';
import { apiFetch } from '../../../shared/api/token';

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

export default function ProjectDetail() {
  const router = useRouter();
  const { id } = router.query;
  const { user } = useAuth();

  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [coverLetter, setCoverLetter] = useState('');
  const [applying, setApplying] = useState(false);
  const [applySuccess, setApplySuccess] = useState(false);
  const [applyError, setApplyError] = useState(null);

  useEffect(() => {
    if (!id) return;
    async function fetchProject() {
      try {
        const res = await apiFetch(`${API_BASE}/projects/all?size=100`);
        if (!res.ok) throw new Error('Failed to load project');
        const data = await res.json();
        const found = (data.content || []).find((p) => p.id === id);
        if (!found) throw new Error('Project not found');
        setProject(found);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    fetchProject();
  }, [id]);

  const handleApply = async (e) => {
    e.preventDefault();
    setApplying(true);
    setApplyError(null);

    try {
      const res = await apiFetch(`${API_BASE}/applications`, {
        method: 'POST',
        body: JSON.stringify({
          projectId: project.id,
          employerId: project.employerId,
          projectTitle: project.title,
          coverLetter,
        }),
      });

      if (res.status === 409) {
        setApplyError('You have already applied to this project.');
        return;
      }
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to submit application');
      }

      setApplySuccess(true);
    } catch (err) {
      setApplyError(err.message);
    } finally {
      setApplying(false);
    }
  };

  if (loading) {
    return (
      <>
        <NavBar />
        <section className="container" style={{ padding: '2rem' }}>
          <p>Loading project...</p>
        </section>
        <Footer />
      </>
    );
  }

  if (error || !project) {
    return (
      <>
        <NavBar />
        <section className="container" style={{ padding: '2rem' }}>
          <p>Error: {error || 'Project not found'}</p>
          <button className="btn-primary" onClick={() => router.push('/listings')}>
            Back to Listings
          </button>
        </section>
        <Footer />
      </>
    );
  }

  const isStudent = user?.role === 'STUDENT';

  return (
    <>
      <NavBar />
      <section className="container" style={{ padding: '2rem', maxWidth: '800px', margin: '0 auto' }}>
        <button
          onClick={() => router.push('/listings')}
          style={{ marginBottom: '1rem', cursor: 'pointer', background: 'none', border: 'none', color: '#6366f1', fontSize: '1rem' }}
        >
          &larr; Back to Listings
        </button>

        <h1>{project.title}</h1>

        <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', margin: '1rem 0' }}>
          {(project.categories || []).map((cat) => (
            <span className="project-tag" key={cat}>
              {CATEGORY_LABELS[cat] || cat}
            </span>
          ))}
        </div>

        <div style={{ margin: '1rem 0', color: '#6b7280', fontSize: '0.9rem' }}>
          <span>Status: <strong>{project.status}</strong></span>
          {project.applicationDeadline && (
            <span style={{ marginLeft: '1.5rem' }}>
              Application deadline: <strong>{new Date(project.applicationDeadline).toLocaleDateString()}</strong>
            </span>
          )}
          {project.projectDeadline && (
            <span style={{ marginLeft: '1.5rem' }}>
              Project deadline: <strong>{new Date(project.projectDeadline).toLocaleDateString()}</strong>
            </span>
          )}
        </div>

        <div style={{ margin: '1.5rem 0', lineHeight: '1.6' }}>
          {project.description}
        </div>

        {isStudent && !applySuccess && (
          <form onSubmit={handleApply} style={{ marginTop: '2rem', padding: '1.5rem', background: '#f9fafb', borderRadius: '8px' }}>
            <h3 style={{ marginBottom: '1rem' }}>Apply to this Project</h3>
            <div className="form-group">
              <label htmlFor="coverLetter">Cover Letter</label>
              <textarea
                id="coverLetter"
                value={coverLetter}
                onChange={(e) => setCoverLetter(e.target.value)}
                placeholder="Tell the employer why you're a great fit for this project..."
                rows={5}
                style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid #d1d5db', resize: 'vertical' }}
              />
            </div>
            {applyError && <p style={{ color: '#ef4444', marginBottom: '0.5rem' }}>{applyError}</p>}
            <button className="btn-primary" type="submit" disabled={applying}>
              {applying ? 'Submitting...' : 'Submit Application'}
            </button>
          </form>
        )}

        {applySuccess && (
          <div style={{ marginTop: '2rem', padding: '1.5rem', background: '#ecfdf5', borderRadius: '8px', color: '#065f46' }}>
            <strong>Application submitted!</strong> You can track its status in your student portal.
          </div>
        )}

        {!user && (
          <div style={{ marginTop: '2rem', padding: '1.5rem', background: '#f9fafb', borderRadius: '8px' }}>
            <p>Log in as a student to apply to this project.</p>
            <button className="btn-primary" onClick={() => window.openLoginModal?.()}>
              Log In
            </button>
          </div>
        )}
      </section>
      <Footer />
    </>
  );
}
