import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import { useState, useEffect } from 'react';
import { useAuth } from '../../shared/context/AuthContext';
import { apiFetch } from '../../shared/api/token';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

const CATEGORY_OPTIONS = [
  { value: 'SOFTWARE_ENGINEERING', label: 'Software Engineering' },
  { value: 'DATA_SCIENCE_ANALYTICS', label: 'Data Science & Analytics' },
  { value: 'AI_MACHINE_LEARNING', label: 'AI & Machine Learning' },
  { value: 'CYBERSECURITY', label: 'Cybersecurity' },
  { value: 'UI_UX_DESIGN', label: 'UI/UX Design' },
  { value: 'CONTENT_WRITING', label: 'Content Writing' },
  { value: 'GRAPHIC_DESIGN_MULTIMEDIA', label: 'Graphic Design & Multimedia' },
  { value: 'RESEARCH', label: 'Research' },
];

export default function EmployerProjects() {
  const { user } = useAuth();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState(null);

  const [form, setForm] = useState({
    title: '',
    description: '',
    categories: [],
    applicationDeadline: '',
    projectDeadline: '',
  });

  useEffect(() => {
    async function fetchProjects() {
      try {
        const res = await apiFetch(`${API_BASE}/projects/all?size=100`);
        if (res.ok) {
          const data = await res.json();
          const myProjects = (data.content || []).filter(
            (p) => p.employerId === user?.id
          );
          setProjects(myProjects);
        }
      } catch (err) {
        console.error('Failed to load projects:', err);
      } finally {
        setLoading(false);
      }
    }
    if (user) fetchProjects();
    else setLoading(false);
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleCategoryToggle = (cat) => {
    setForm((prev) => {
      const cats = prev.categories.includes(cat)
        ? prev.categories.filter((c) => c !== cat)
        : [...prev.categories, cat];
      return { ...prev, categories: cats };
    });
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setFormError(null);

    try {
      const payload = {
        title: form.title,
        description: form.description,
        categories: form.categories,
        applicationDeadline: new Date(form.applicationDeadline).toISOString(),
        projectDeadline: new Date(form.projectDeadline).toISOString(),
      };

      const res = await apiFetch(`${API_BASE}/projects`, {
        method: 'POST',
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to create project');
      }

      const created = await res.json();
      setProjects((prev) => [created, ...prev]);
      setShowForm(false);
      setForm({ title: '', description: '', categories: [], applicationDeadline: '', projectDeadline: '' });
    } catch (err) {
      setFormError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <NavBar />
      <section className="container" style={{ padding: '2rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <div>
            <h1 className="dashboard-title">My Projects</h1>
            <p className="dashboard-subtitle">Manage your project listings</p>
          </div>
          <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancel' : '+ Create Project'}
          </button>
        </div>

        {showForm && (
          <form onSubmit={handleCreate} style={{ padding: '1.5rem', background: '#f9fafb', borderRadius: '8px', marginBottom: '2rem' }}>
            <h3 style={{ marginBottom: '1rem' }}>New Project</h3>
            <div className="form-group">
              <label>Title</label>
              <input type="text" name="title" required value={form.title} onChange={handleChange}
                style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #d1d5db' }} />
            </div>
            <div className="form-group">
              <label>Description</label>
              <textarea name="description" required value={form.description} onChange={handleChange} rows={4}
                style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #d1d5db', resize: 'vertical' }} />
            </div>
            <div className="form-group">
              <label>Categories</label>
              <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                {CATEGORY_OPTIONS.map((cat) => (
                  <button key={cat.value} type="button"
                    onClick={() => handleCategoryToggle(cat.value)}
                    style={{
                      padding: '0.4rem 0.8rem', borderRadius: '9999px', border: '1px solid #d1d5db', cursor: 'pointer',
                      background: form.categories.includes(cat.value) ? '#6366f1' : '#fff',
                      color: form.categories.includes(cat.value) ? '#fff' : '#374151',
                    }}>
                    {cat.label}
                  </button>
                ))}
              </div>
            </div>
            <div style={{ display: 'flex', gap: '1rem' }}>
              <div className="form-group" style={{ flex: 1 }}>
                <label>Application Deadline</label>
                <input type="date" name="applicationDeadline" required value={form.applicationDeadline} onChange={handleChange}
                  style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #d1d5db' }} />
              </div>
              <div className="form-group" style={{ flex: 1 }}>
                <label>Project Deadline</label>
                <input type="date" name="projectDeadline" required value={form.projectDeadline} onChange={handleChange}
                  style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #d1d5db' }} />
              </div>
            </div>
            {formError && <p style={{ color: '#ef4444' }}>{formError}</p>}
            <button className="btn-primary" type="submit" disabled={submitting}>
              {submitting ? 'Creating...' : 'Create Project'}
            </button>
          </form>
        )}

        {loading && <p>Loading projects...</p>}
        {!loading && projects.length === 0 && !showForm && (
          <p>No projects yet. Create your first project listing!</p>
        )}
        {!loading && projects.length > 0 && (
          <table className="dashboard-table">
            <thead>
              <tr>
                <th>Title</th>
                <th>Status</th>
                <th>Deadline</th>
                <th>Created</th>
              </tr>
            </thead>
            <tbody>
              {projects.map((p) => (
                <tr key={p.id}>
                  <td>{p.title}</td>
                  <td>
                    <span className="dashboard-status-active">{p.status}</span>
                  </td>
                  <td>{p.applicationDeadline ? new Date(p.applicationDeadline).toLocaleDateString() : '-'}</td>
                  <td>{new Date(p.createdAt).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
      <Footer />
    </>
  );
}
