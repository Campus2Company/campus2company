import React, { useState } from 'react';

export default function AuthModal({ type, isOpen, onClose }) {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: '',
    department: '',
    university: ''
  });

  // department / university options for students
  // we render these with optgroups so students can choose a specific subject
  const departmentGroups = [
    {
      label: 'Arts, Humanities & Social Sciences',
      options: [
        { value: 'english', label: 'English' },
        { value: 'history', label: 'History' },
        { value: 'philosophy', label: 'Philosophy' },
        { value: 'modern-languages', label: 'Modern Languages (French, German, Spanish, etc.)' },
        { value: 'sociology', label: 'Sociology' },
        { value: 'politics', label: 'Politics' },
        { value: 'music', label: 'Music' }
      ]
    },
    {
      label: 'Science & Engineering',
      options: [
        { value: 'physics', label: 'Physics' },
        { value: 'chemistry', label: 'Chemistry' },
        { value: 'biology', label: 'Biology' },
        { value: 'maths-stats', label: 'Mathematics & Statistics' },
        { value: 'computer-science', label: 'Computer Science' },
        { value: 'civil-engineering', label: 'Civil Engineering' },
        { value: 'mechanical-engineering', label: 'Mechanical Engineering' },
        { value: 'electronic-engineering', label: 'Electronic Engineering' }
      ]
    },
    {
      label: 'Business & Law',
      options: [
        { value: 'accounting-finance', label: 'Accounting & Finance' },
        { value: 'economics', label: 'Economics' },
        { value: 'management', label: 'Management' },
        { value: 'marketing', label: 'Marketing' },
        { value: 'law', label: 'School of Law' }
      ]
    },
    {
      label: 'Health Sciences',
      options: [
        { value: 'medicine', label: 'Medicine' },
        { value: 'nursing', label: 'Nursing & Midwifery' },
        { value: 'pharmacy', label: 'Pharmacy' },
        { value: 'occupational-therapy', label: 'Occupational Therapy' }
      ]
    },
    {
      label: 'Maynooth University +10',
      options: []
    }
  ];

  const universityOptions = [
    { value: '', label: 'Select your university' },
    { value: 'ul', label: 'University of Limerick' },
    { value: 'nuig', label: 'National University of Galway' },
    { value: 'ucd', label: 'University College Dublin' }
  ];

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`${type} submitted`);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal active" id={`${type}Modal`}>
      <div className="modal-overlay" id={`${type}Overlay`} onClick={onClose} />
      <div className="modal-content">
        <button className="modal-close" id={`${type}Close`} onClick={onClose}>&times;</button>
        <h2>{type === 'login' ? 'Login' : 'Sign Up'}</h2>
        <form className="auth-form" onSubmit={handleSubmit} id={`${type}Form`}>
          {type === 'signup' && (
            <div className="form-group">
              <label htmlFor="signupName">Full Name</label>
              <input
                type="text"
                id="signupName"
                name="name"
                required
                placeholder="John Doe"
                value={formData.name}
                onChange={handleChange}
              />
            </div>
          )}
          <div className="form-group">
            <label htmlFor={`${type}Email`}>Email</label>
            <input
              type="email"
              id={`${type}Email`}
              name="email"
              required
              placeholder="your.email@example.com"
              value={formData.email}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor={`${type}Password`}>Password</label>
            <input
              type="password"
              id={`${type}Password`}
              name="password"
              required
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
            />
          </div>
          {type === 'signup' && (
            <>
              <div className="form-group">
                <label htmlFor="signupRole">Role</label>
                <select
                  id="signupRole"
                  name="role"
                  required
                  value={formData.role}
                  onChange={handleChange}
                >
                  <option value="">Select your role</option>
                  <option value="student">Student</option>
                  <option value="business">Business</option>
                  <option value="lecturer">Lecturer</option>
                  <option value="admin">Admin</option>
                </select>
              </div>

              {/* additional fields shown only when the selected role is student */}
              {formData.role === 'student' && (
                <>
                  <div className="form-group">
                    <label htmlFor="signupDepartment">Department</label>
                    <select
                      id="signupDepartment"
                      name="department"
                      required
                      value={formData.department}
                      onChange={handleChange}
                    >
                      <option value="">Select your department</option>
                      {departmentGroups.map(group => (
                        <optgroup key={group.label} label={group.label}>
                          {group.options.map(opt => (
                            <option key={opt.value} value={opt.value}>
                              {opt.label}
                            </option>
                          ))}
                        </optgroup>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupUniversity">University</label>
                    <select
                      id="signupUniversity"
                      name="university"
                      required
                      value={formData.university}
                      onChange={handleChange}
                    >
                      {universityOptions.map(opt => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>
                </>
              )}
            </>
          )}
          <button type="submit" className="btn-primary btn-full">
            {type === 'login' ? 'Login' : 'Sign Up'}
          </button>
        </form>
      </div>
    </div>
  );
}
