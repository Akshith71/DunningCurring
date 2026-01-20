import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';


// Pages
import Login from './pages/Login_Temp'; 
import CustomerDashboard from './pages/CustomerDashboard';
import AdminDashboard from './pages/AdminDashboard';
import Support from './pages/Support'; // ✅ 1. IMPORT THIS

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          {/* Public Route */}
          <Route path="/login" element={<Login />} />

          {/* SECURE CUSTOMER ROUTES */}
          <Route element={<ProtectedRoute allowedRoles={['ROLE_CUSTOMER']} />}>
            <Route path="/customer" element={<CustomerDashboard />} />
            <Route path="/support" element={<Support />} />
          </Route>

          {/* SECURE ADMIN ROUTE */}
          <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
            <Route path="/admin" element={<AdminDashboard />} />
          </Route>
          
          {/* Default Redirect */}
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;