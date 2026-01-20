import React, { createContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decoded = jwtDecode(token);
        // Extract claims consistent with your Java backend
        setUser({ 
            username: decoded.sub, 
            role: decoded.role, 
            customerId: decoded.customerId 
        });
      } catch (e) {
        localStorage.removeItem('token');
      }
    }
  }, []);

  const login = (token) => {
    localStorage.setItem('token', token);
    const decoded = jwtDecode(token);
    setUser({ 
        username: decoded.sub, 
        role: decoded.role,
        customerId: decoded.customerId 
    });

    // Role-based redirect
    if (decoded.role === 'ROLE_ADMIN') {
        navigate('/admin');
    } else {
        navigate('/customer');
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
    navigate('/login');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};