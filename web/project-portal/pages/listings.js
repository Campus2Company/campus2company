import NavBar from '../../shared/components/NavBar';
import Footer from '../../shared/components/Footer';
import ProjectCard from '../components/ProjectCard';
import { useState } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../../shared/context/AuthContext';

const mockProjects = [
  {
    id: '1',
    title: 'AI-Powered Customer Service Platform',
    company: 'TechCorp Industries',
    description: 'Develop an intelligent chatbot system using natural language processing to enhance customer support efficiency. This project involves creating a scalable backend and integrating with APIs.',
    tags: ['Python', 'Machine Learning', 'API Development'],
    time: '3 months'
  },
  {
    id: '2',
    title: 'Sustainable Supply Chain Analysis',
    company: 'GreenLogistics Ltd',
    description: 'Research and analyze sustainable practices in supply chain management. Help identify opportunities for reducing carbon footprint and implementing eco-friendly solutions.',
    tags: ['Data Analysis', 'Supply Chain', 'Sustainability'],
    time: '4 months'
  },
  {
    id: '3',
    title: 'Mobile App UI/UX Redesign',
    company: 'FinanceFlow',
    description: 'Redesign the user interface and experience of our mobile banking application. Focus on improving accessibility, user flow, and modern design principles.',
    tags: ['Figma', 'UI/UX Design', 'User Research'],
    time: '2 months'
  }
];

export default function ListingsPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('All Categories');

  const filtered = mockProjects.filter(
    (project) =>
      (category === 'All Categories' || project.tags.includes(category)) &&
      (project.title.toLowerCase().includes(search.toLowerCase()) ||
        project.company.toLowerCase().includes(search.toLowerCase()) ||
        project.description.toLowerCase().includes(search.toLowerCase()))
  );

  const allTags = Array.from(new Set(mockProjects.flatMap((project) => project.tags)));

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
            {allTags.map((tag) => (
              <option key={tag}>{tag}</option>
            ))}
          </select>
        </div>
        <div className="listings-grid">
          {filtered.map((project, index) => (
            <ProjectCard
              key={index}
              {...project}
              onViewDetails={() => handleViewDetails(project)}
            />
          ))}
        </div>
      </section>
      <Footer />
    </>
  );
}