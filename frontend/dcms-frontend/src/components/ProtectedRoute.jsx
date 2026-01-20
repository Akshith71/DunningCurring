import React, { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const ProtectedRoute = ({ allowedRoles }) => {
  const { user } = useContext(AuthContext);

  // 1. If no user is logged in, redirect to Login
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // 2. If user exists but doesn't have the correct role (e.g. CUSTOMER trying to access ADMIN)
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/login" replace />;
  }

  // 3. If all checks pass, render the child route (Outlet)
  return <Outlet />;
};

export default ProtectedRoute;