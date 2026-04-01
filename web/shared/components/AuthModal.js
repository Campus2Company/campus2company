import React, { useEffect, useRef, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Eye, EyeOff } from 'lucide-react';

export default function AuthModal({ type, isOpen, onClose }) {
  const { login, goToPortal } = useAuth();
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [signupStep, setSignupStep] = useState(1);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const submitAbortRef = useRef(null);
  const submitSeqRef = useRef(0);

  const { openSignup } = useAuth();

  const firstNameRef = useRef(null);
  const lastNameRef = useRef(null);
  const emailRef = useRef(null);
  const passwordRef = useRef(null);
  const confirmPasswordRef = useRef(null);

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: '',
    faculty: '',
    course: '',
    studyLevel: '',
    bio: '',
    universityId: '',
    companyName: '',
    industry: '',
    description: '',
    websiteUrl: ''
  });

  const studyLevelOptions = [
    { value: '', label: 'Select your study level' },
    { value: 'UNDERGRADUATE', label: 'Undergraduate' },
    { value: 'MASTERS', label: 'Masters' }
  ];

  const industryOptions = [
    { value: '', label: 'Select industry' },
    { value: 'technology', label: 'Technology' },
    { value: 'finance', label: 'Finance' },
    { value: 'healthcare', label: 'Healthcare' },
    { value: 'education', label: 'Education' },
    { value: 'retail', label: 'Retail' },
    { value: 'manufacturing', label: 'Manufacturing' },
    { value: 'construction', label: 'Construction' },
    { value: 'hospitality', label: 'Hospitality' },
    { value: 'other', label: 'Other' }
  ];

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
    { value: '44118681-59d9-4854-b95d-da5fd5396b4d', label: 'University of Limerick' },
    { value: 'nuig', label: 'National University of Galway' },
    { value: 'ucd', label: 'University College Dublin' }
  ];

  const handleChange = (e) => {
    if (error) setError('');
    if (notice) setNotice('');
    const { name, value } = e.target;
    setFormData((prev) => {
      if (name === 'faculty') {
        return { ...prev, faculty: value, course: '' };
      }
      return { ...prev, [name]: value };
    });
  };

  const setRole = (role) => {
    if (error) setError('');
    if (notice) setNotice('');
    setFormData((prev) => {
      if (prev.role === role) return prev;
      const clearingStudentFields = role !== 'STUDENT'
        ? { faculty: '', course: '', studyLevel: '', bio: '', universityId: '' }
        : {};

      const clearingBusinessFields = role !== 'EMPLOYER'
        ? { companyName: '', industry: '', description: '', websiteUrl: '' }
        : {};

      return {
        ...prev,
        role,
        ...clearingStudentFields,
        ...clearingBusinessFields
      };
    });

    // If user changes account type while on step 2,
    // keep them on step 2 but show correct fields.
  };

  const validateStep1 = () => {
    if (!formData.role) {
      setError('Please choose Student or Business.');
      return false;
    }

    // Trigger native browser validation UI where possible
    if (firstNameRef.current && !firstNameRef.current.checkValidity()) {
      firstNameRef.current.reportValidity();
      return false;
    }
    if (lastNameRef.current && !lastNameRef.current.checkValidity()) {
      lastNameRef.current.reportValidity();
      return false;
    }
    if (emailRef.current && !emailRef.current.checkValidity()) {
      emailRef.current.reportValidity();
      return false;
    }
    if (passwordRef.current && !passwordRef.current.checkValidity()) {
      passwordRef.current.reportValidity();
      return false;
    }
    if (confirmPasswordRef.current && !confirmPasswordRef.current.checkValidity()) {
      confirmPasswordRef.current.reportValidity();
      return false;
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match.');
      return false;
    }

    return true;
  };

  const handleNext = () => {
    if (type !== 'signup') return;
    if (error) setError('');
    if (notice) setNotice('');
    if (!validateStep1()) return;
    setSignupStep(2);
  };

  const handleBack = () => {
    if (type !== 'signup') return;
    if (submitAbortRef.current) {
      submitAbortRef.current.abort();
      submitAbortRef.current = null;
    }
    setError('');
    setNotice('');
    setSignupStep(1);
  };

  const handleClose = () => {
    if (submitAbortRef.current) {
      submitAbortRef.current.abort();
      submitAbortRef.current = null;
    }
    setError('');
    setNotice('');
    if (type === 'signup') setSignupStep(1);
    setShowPassword(false);
    setShowConfirmPassword(false);
    onClose();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const submitter = e?.nativeEvent?.submitter;
    const action = submitter?.dataset?.action;

    // Hard guard: only allow submits from the explicit submit buttons.
    // This prevents Enter-key submits (which can happen inside forms) from calling the API.
    if (type === 'signup' && action !== 'signup') return;
    if (type === 'login' && action !== 'login') return;

    if (type === 'signup') {
      // Only submit from Step 2
      if (signupStep !== 2) return;
      // Re-check Step 1 constraints before submission
      if (!validateStep1()) {
        setSignupStep(1);
        return;
      }
    }

    const apiBaseUrl = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');
    const registerEndpointPath = formData.role === 'STUDENT' ? '/auth/register/student'
      : '/auth/register/employer';
    const loginEndpointPath = '/auth/login';
    const registerEndpoint = `${apiBaseUrl}${registerEndpointPath}`;
    const loginEndpoint = `${apiBaseUrl}${loginEndpointPath}`;

    const payload =
      type === 'signup'
        ? {
          firstName: formData.firstName,
          lastName: formData.lastName,
          email: formData.email,
          password: formData.password,
          role: formData.role,
          bio: formData.role === 'STUDENT' ? formData.bio : undefined,
          course: formData.role === 'STUDENT' ? formData.course : undefined,
          faculty: formData.role === 'STUDENT' ? formData.faculty : undefined,
          studyLevel: formData.role === 'STUDENT' ? formData.studyLevel : undefined,
          universityId: formData.role === 'STUDENT' ? formData.universityId : undefined,
          companyName: formData.role === 'EMPLOYER' ? formData.companyName : undefined,
          industry: formData.role === 'EMPLOYER' ? formData.industry : undefined,
          description: formData.role === 'EMPLOYER' ? formData.description : undefined,
          websiteUrl: formData.role === 'EMPLOYER' ? formData.websiteUrl : undefined,
        }
        : {
          email: formData.email,
          password: formData.password,
        };

    if (submitAbortRef.current) {
      submitAbortRef.current.abort();
    }

    const controller = new AbortController();
    submitAbortRef.current = controller;
    const seq = (submitSeqRef.current += 1);

    let res;
    try {
      const endpointToCall = type === 'signup' ? registerEndpoint : loginEndpoint;
      res = await fetch(endpointToCall, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
        signal: controller.signal,
        credentials: 'include',
      });
    } catch (err) {
      console.log('Error during auth request:', err);
      setError(err?.message || 'Network error');
      return;
    }

    if (seq !== submitSeqRef.current) return;

    if (!res.ok) {
      const err = await res.json();
      setError(err.message || 'Authentication failed');
      return;
    }

    if (type === 'login') {
      const data = await res.json();
      login(data);
      handleClose();
      goToPortal(data.user?.role);
      return;
    }

    // Signup
    await res.json();

    if (formData.role === 'EMPLOYER') {
      setNotice('Employer account created and pending admin approval. You will be able to log in once approved.');
      return;
    }

    setNotice('Account created successfully! You can now log in.');
  };

  useEffect(() => {
    if (isOpen) return;
    if (submitAbortRef.current) {
      submitAbortRef.current.abort();
      submitAbortRef.current = null;
    }
    setShowPassword(false);
    setShowConfirmPassword(false);
  }, [isOpen]);

  useEffect(() => {
    if (typeof window === 'undefined') return;
    if (typeof document === 'undefined') return;
    if (!isOpen) return;

    const body = document.body;
    const prevOverflow = body.style.overflow;
    const prevPaddingRight = body.style.paddingRight;

    const scrollBarWidth = window.innerWidth - document.documentElement.clientWidth;
    body.style.overflow = 'hidden';

    if (scrollBarWidth > 0) {
      const computedPaddingRight = parseFloat(window.getComputedStyle(body).paddingRight) || 0;
      body.style.paddingRight = `${computedPaddingRight + scrollBarWidth}px`;
    }

    return () => {
      body.style.overflow = prevOverflow;
      body.style.paddingRight = prevPaddingRight;
    };
  }, [isOpen]);

  if (!isOpen) return null;

  const isSignup = type === 'signup';
  const showStep1 = !isSignup || signupStep === 1;
  const showStep2 = isSignup && signupStep === 2;

  const facultyOptions = [
    { value: '', label: 'Select your faculty' },
    ...departmentGroups.map((g) => ({ value: g.label, label: g.label }))
  ];

  const courseOptions = (
    departmentGroups.find((g) => g.label === formData.faculty)?.options ?? []
  );

  const handleFormKeyDown = (e) => {
    if (type !== 'signup') return;
    if (e.key !== 'Enter') return;
    e.preventDefault();
  };

  return (
    <div className="modal active" id={`${type}Modal`}>
      <div className="modal-overlay" id={`${type}Overlay`} onClick={handleClose} />
      <div className="modal-content">
        <button className="modal-close" id={`${type}Close`} onClick={handleClose}>&times;</button>
        <h2>{type === 'login' ? 'Login' : 'Sign Up'}</h2>
        {isSignup ? <div className="step-indicator">Step {signupStep} of 2</div> : null}
        {notice ? <div className="form-notice" role="status">{notice}</div> : null}
        {error ? <div className="form-error" role="alert">{error}</div> : null}
        <form className="auth-form" onSubmit={handleSubmit} onKeyDown={handleFormKeyDown} id={`${type}Form`}>
          {showStep1 && type === 'signup' && (
            <>
              <div className="form-group">
                <label>Account type</label>
                <div className="role-buttons">
                  <button
                    type="button"
                    className={`role-button ${formData.role === 'STUDENT' ? 'active' : ''}`}
                    onClick={() => setRole('STUDENT')}
                  >
                    Student
                  </button>
                  <button
                    type="button"
                    className={`role-button ${formData.role === 'EMPLOYER' ? 'active' : ''}`}
                    onClick={() => setRole('EMPLOYER')}
                  >
                    Business
                  </button>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="signupFirstName">First Name</label>
                <input
                  type="text"
                  id="signupFirstName"
                  name="firstName"
                  required
                  placeholder="John"
                  value={formData.firstName}
                  onChange={handleChange}
                  ref={firstNameRef}
                />
              </div>

              <div className="form-group">
                <label htmlFor="signupLastName">Last Name</label>
                <input
                  type="text"
                  id="signupLastName"
                  name="lastName"
                  required
                  placeholder="Doe"
                  value={formData.lastName}
                  onChange={handleChange}
                  ref={lastNameRef}
                />
              </div>
            </>

          )}
          {showStep1 && (
            <>
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
                  ref={emailRef}
                />
              </div>
              <div className="form-group">
                <label htmlFor={`${type}Password`}>Password</label>
                <div className="password-field">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    id={`${type}Password`}
                    name="password"
                    required
                    placeholder="Enter your password"
                    value={formData.password}
                    onChange={handleChange}
                    ref={passwordRef}
                  />
                  <button
                    type="button"
                    className="password-toggle"
                    aria-label={showPassword ? 'Hide password' : 'Show password'}
                    onClick={() => setShowPassword((v) => !v)}
                  >
                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
              </div>
              {type === 'signup' ? (
                <div className="form-group">
                  <label htmlFor="signupConfirmPassword">Confirm Password</label>
                  <div className="password-field">
                    <input
                      type={showConfirmPassword ? 'text' : 'password'}
                      id="signupConfirmPassword"
                      name="confirmPassword"
                      required
                      placeholder="Confirm your password"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      ref={confirmPasswordRef}
                    />
                    <button
                      type="button"
                      className="password-toggle"
                      aria-label={showConfirmPassword ? 'Hide password' : 'Show password'}
                      onClick={() => setShowConfirmPassword((v) => !v)}
                    >
                      {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                    </button>
                  </div>
                </div>
              ) : null}
              {type === 'signup' && (
                <button type="button" className="btn-primary btn-full" onClick={handleNext}>
                  Next
                </button>
              )}
            </>
          )}

          {showStep2 && type === 'signup' && (
            <>
              {formData.role === 'STUDENT' && (
                <>
                  <div className="form-group">
                    <label htmlFor="signupFaculty">Faculty</label>
                    <select
                      id="signupFaculty"
                      name="faculty"
                      required
                      value={formData.faculty}
                      onChange={handleChange}
                    >
                      {facultyOptions.map((opt) => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupCourse">Course</label>
                    <select
                      id="signupCourse"
                      name="course"
                      required
                      value={formData.course}
                      onChange={handleChange}
                    >
                      <option value="">Select your course</option>
                      {courseOptions.map((opt) => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupStudyLevel">Study Level</label>
                    <select
                      id="signupStudyLevel"
                      name="studyLevel"
                      required
                      value={formData.studyLevel}
                      onChange={handleChange}
                    >
                      {studyLevelOptions.map((opt) => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupUniversityId">University</label>
                    <select
                      id="signupUniversityId"
                      name="universityId"
                      required
                      value={formData.universityId}
                      onChange={handleChange}
                    >
                      {universityOptions.map(opt => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupBio">Bio (optional)</label>
                    <textarea
                      id="signupBio"
                      name="bio"
                      placeholder="Tell us about yourself"
                      value={formData.bio}
                      onChange={handleChange}
                      rows={3}
                      maxLength={1000}
                    />
                  </div>
                </>
              )}

              {formData.role === 'EMPLOYER' && (
                <>
                  <div className="form-group">
                    <label htmlFor="signupCompanyName">Company Name</label>
                    <input
                      type="text"
                      id="signupCompanyName"
                      name="companyName"
                      required
                      placeholder="Acme Ltd"
                      value={formData.companyName}
                      onChange={handleChange}
                      maxLength={255}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupIndustry">Industry</label>
                    <select
                      id="signupIndustry"
                      name="industry"
                      required={false}
                      value={formData.industry}
                      onChange={handleChange}
                    >
                      {industryOptions.map((opt) => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupDescription">Description</label>
                    <textarea
                      id="signupDescription"
                      name="description"
                      placeholder="Tell us about your company"
                      value={formData.description}
                      onChange={handleChange}
                      rows={4}
                      maxLength={2000}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="signupWebsiteUrl">Website</label>
                    <input
                      type="url"
                      id="signupWebsiteUrl"
                      name="websiteUrl"
                      required={false}
                      placeholder="https://example.com"
                      value={formData.websiteUrl}
                      onChange={handleChange}
                      maxLength={500}
                    />
                  </div>
                </>
              )}


              <button type="button" className="btn-text btn-full" onClick={handleBack}>
                Back
              </button>
            </>
          )}

          {type === 'login' ? (
            <div>
              <div className='login-footer'>
                <p>Do not have an account?</p>
                <a onClick={openSignup} style={{ cursor: 'pointer' }}>Register</a>
              </div>

              <button type="submit" data-action="login" className="btn-primary btn-full">
                Login
              </button>
            </div>
          ) : showStep2 && (
            <button type="submit" data-action="signup" className="btn-primary btn-full">
              Sign Up
            </button>
          )}
        </form>
      </div>
    </div>
  );
}
