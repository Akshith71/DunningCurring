import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import './Support.css'; 

const Support = () => {
  const navigate = useNavigate();
  const [subject, setSubject] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  
  // State to store the fetched Customer ID
  const [customerId, setCustomerId] = useState(null);

  // ✅ NEW: Fetch Customer Profile on Page Load
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await api.get('/api/customer/profile');
        if (response.data && response.data.id) {
          setCustomerId(response.data.id);
          console.log("✅ Logged in as Customer ID:", response.data.id);
        }
      } catch (error) {
        console.error("Profile Fetch Error:", error);
        setMessage("⚠️ Could not verify identity. Please log in again.");
        // Optional: Redirect to login if token is invalid
        // setTimeout(() => navigate('/login'), 2000);
      }
    };
    fetchProfile();
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    if (!customerId) {
      setMessage("⚠️ Error: You must be logged in (Customer ID missing).");
      setLoading(false);
      return;
    }

    try {
      // ✅ Now using the fetched 'customerId'
      await api.post(`/api/customer/${customerId}/tickets`, {
        subject: subject,
        description: description
      });

      setMessage("✅ Ticket raised successfully! We will contact you soon.");
      setSubject('');
      setDescription('');
      setTimeout(() => navigate('/customer'), 2000);

    } catch (error) {
      console.error("Ticket Error:", error);
      setMessage("❌ Failed to raise ticket. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="support-container">
      <div className="support-card">
        <h2>🎫 Customer Support</h2>
        <p>How can we help you today?</p>

        {message && <div className={`alert ${message.includes('✅') ? 'success' : 'error'}`}>{message}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Subject / Issue Type</label>
            <select value={subject} onChange={(e) => setSubject(e.target.value)} required>
              <option value="">Select an issue...</option>
              <option value="Internet Slow">Internet Slow</option>
              <option value="Billing Issue">Billing Issue</option>
              <option value="Service Disconnection">Service Disconnection</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label>Description of your issue</label>
            <textarea 
              rows="5"
              value={description} 
              onChange={(e) => setDescription(e.target.value)} 
              placeholder="Please describe the problem in detail..." 
              required
            />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-secondary" onClick={() => navigate('/customer')}>Cancel</button>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Submitting...' : 'Submit Ticket'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Support;