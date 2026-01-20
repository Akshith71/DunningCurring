import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import { AuthContext } from '../context/AuthContext';
import './Login.css';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useContext(AuthContext);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // ✅ HELPER: Decode Token to get Role/User info
  const parseJwt = (token) => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (e) {
      return null;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // 1. Send Login Request
      const response = await api.post('/auth/login', null, {
        params: { username, password }
      });
      
      // 2. Handle Response (String vs Object)
      let token = '';
      let user = null;

      if (typeof response.data === 'string') {
          // Case A: Backend returns just the token string
          token = response.data;
      } else {
          // Case B: Backend returns JSON { token: "...", user: ... }
          token = response.data.token || response.data.jwt;
          user = response.data.user;
      }

      // 3. Decode Token to find Role (Critical for Admin)
      const decoded = parseJwt(token);
      console.log("Decoded Token:", decoded); // Debugging

      // Extract Role (Backend might store it as 'role', 'roles', or 'authorities')
      const role = user?.role || decoded?.role || decoded?.roles || decoded?.authorities || '';
      
      // Extract User ID if missing (needed for Customer Dashboard)
      // If backend doesn't send User Object, we might need to rely on 'sub' or 'id' from token
      const userId = user?.id || decoded?.id || decoded?.userId;

      // 4. Construct User Object to Save
      const userToSave = user || { 
          username: decoded?.sub || username, 
          role: role,
          id: userId 
      };

      // 5. Save to Storage
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userToSave));

      // Update Context
      if (login) login(token);

      // 6. REDIRECTION LOGIC
      // Check for Admin Role (handles 'ROLE_ADMIN', 'ADMIN', etc.)
      const roleString = JSON.stringify(role).toUpperCase();
      
      if (roleString.includes('ADMIN')) {
          console.log("Redirecting to Admin...");
          navigate('/admin', { replace: true });
      } else {
          console.log("Redirecting to Customer...");
          navigate('/customer', { replace: true });
      }

    } catch (err) {
      console.error("Login Failed:", err);
      setError('Invalid Credentials or Server Error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Welcome Back</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Username</label>
            <input 
              type="text" 
              className="form-input"
              value={username} 
              onChange={(e) => setUsername(e.target.value)} 
              required
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input 
              type="password" 
              className="form-input"
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              required
            />
          </div>
          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? 'Signing In...' : 'Sign In'}
          </button>
        </form>
        {error && <div className="error-msg">{error}</div>}
      </div>
    </div>
  );
};

export default Login;