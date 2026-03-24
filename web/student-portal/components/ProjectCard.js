export default function ProjectCard({ title, company, description, tags, time, onViewDetails }) {
  return (
    <div className="project-card">
      <h3>{title}</h3>
      <div className="project-company">{company}</div>
      <div className="project-desc">{description}</div>
      <div className="project-tags">
        {tags.map((tag, i) => (
          <span className="project-tag" key={i}>{tag}</span>
        ))}
      </div>
      <div className="project-footer">
        <span className="project-time">{time}</span>
        <button className="btn-primary" onClick={onViewDetails}>View Details</button>
      </div>
    </div>
  );
}