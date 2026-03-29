import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import ProjectCard from '../components/ProjectCard';
import { useState } from 'react';

const mockProjects = [
  {
    title: 'AI-Powered Customer Service Platform',
    company: 'TechCorp Industries',
    description: 'Develop an intelligent chatbot system using natural language processing to enhance customer support efficiency. This project involves creating a scalable backend and integrating with APIs.',
    tags: ['Python', 'Machine Learning', 'API Development'],
    time: '3 months',
  },
  {
    title: 'Sustainable Supply Chain Analysis',
    company: 'GreenLogistics Ltd',
    description: 'Research and analyze sustainable practices in supply chain management. Help identify opportunities for reducing carbon footprint and implementing eco-friendly solutions.',
    tags: ['Data Analysis', 'Supply Chain', 'Sustainability'],
    time: '4 months',
  },
  {
    title: 'Mobile App UI/UX Redesign',
    company: 'FinanceFlow',
    description: 'Redesign the user interface and experience of our mobile banking application. Focus on improving accessibility, user flow, and modern design principles.',
    tags: ['Figma', 'UI/UX Design', 'User Research'],
    time: '2 months',
  },
];

export default function Listings() {
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('All Categories');

  const filtered = mockProjects.filter(p =>
    (category === 'All Categories' || p.tags.includes(category)) &&
    (p.title.toLowerCase().includes(search.toLowerCase()) ||
      p.company.toLowerCase().includes(search.toLowerCase()) ||
      p.description.toLowerCase().includes(search.toLowerCase()))
  );

  const allTags = Array.from(new Set(mockProjects.flatMap(p => p.tags)));

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
            onChange={e => setSearch(e.target.value)}
            className="listings-search"
          />
          <select
            value={category}
            onChange={e => setCategory(e.target.value)}
            className="listings-category"
          >
            <option>All Categories</option>
            {allTags.map(tag => (
              <option key={tag}>{tag}</option>
            ))}
          </select>
        </div>
        <div className="listings-grid">
          {filtered.map((p, i) => (
            <ProjectCard key={i} {...p} onViewDetails={() => {}} />
          ))}
        </div>
      </section>
      <Footer />
    </>
  );
}
