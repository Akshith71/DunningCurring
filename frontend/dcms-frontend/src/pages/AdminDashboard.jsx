import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

// --- Component Imports ---
import CustomerList from '../components/admin/CustomerList';
import RegisterCustomer from '../components/admin/RegisterCustomer';
import RuleEngine from '../components/admin/RuleEngine';
import SimulatePayment from '../components/admin/SimulatePayment';
import DunningLogs from '../components/admin/DunningLogs';
import TicketManager from '../components/admin/TicketManager'; // Kept Ticket Manager

import './AdminDashboard.css';

const AdminDashboard = () => {
  const navigate = useNavigate();
  
  // Navigation State
  const [activeView, setActiveView] = useState('dashboard');
  
  // Dashboard Data State
  const [stats, setStats] = useState({ total: 0, active: 0, blocked: 0, overdueAmt: 0 });
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  useEffect(() => {
    fetchStats();
  }, [refreshTrigger]);

  const fetchStats = async () => {
    try {
      const res = await api.get('/api/admin/customers/customers');
      const customers = res.data;
      
      const total = customers.length;
      const overdueAmt = customers.reduce((sum, c) => sum + (c.overdueAmount || 0), 0);
      
      const blocked = customers.filter(c => c.status === 'BLOCKED' || c.overdueDays > 0).length; 
      const active = total - blocked;

      setStats({ total, active, blocked, overdueAmt });
    } catch (error) {
      console.error("Failed to fetch dashboard stats", error);
    }
  };

  const refreshDashboard = () => {
    fetchStats(); 
    setRefreshTrigger(prev => prev + 1); 
  };

  const handleLogout = () => {
    if(window.confirm("Are you sure you want to log out?")) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
    }
  };

  const handleGlobalDunning = async () => {
    if(window.confirm("⚠️ Run Dunning Engine for ALL customers?")) {
        try {
            alert("Dunning Batch Job Started...");
            const customersRes = await api.get('/api/admin/customers/customers');
            await Promise.all(customersRes.data.map(c => api.post(`/api/admin/dunning/apply/${c.id}`)));
            alert("Global Dunning Run Complete!");
            refreshDashboard();
        } catch (e) {
            alert("Error running global dunning process.");
        }
    }
  };

  return (
    <div className="dashboard-container">
      
      {/* --- SIDEBAR --- */}
      <aside className="sidebar">
        <div className="sidebar-header">
          DCMS Admin
        </div>
        
        <nav className="sidebar-nav">
          <button onClick={() => setActiveView('dashboard')} className={`nav-btn ${activeView === 'dashboard' ? 'active' : ''}`}>
            📊 Overview
          </button>
          
          <button onClick={() => setActiveView('register')} className={`nav-btn ${activeView === 'register' ? 'active' : ''}`}>
            ➕ Register New
          </button>
          
          <button onClick={() => setActiveView('rules')} className={`nav-btn ${activeView === 'rules' ? 'active' : ''}`}>
            ⚙️ Rule Engine
          </button>
          
          <button onClick={() => setActiveView('simulate')} className={`nav-btn ${activeView === 'simulate' ? 'active' : ''}`}>
            📨 Simulate Notif.
          </button>

          <button onClick={() => setActiveView('logs')} className={`nav-btn ${activeView === 'logs' ? 'active' : ''}`}>
            📜 Dunning Logs
          </button>

          <button onClick={() => setActiveView('tickets')} className={`nav-btn ${activeView === 'tickets' ? 'active' : ''}`}>
            🎫 Support Tickets
          </button>
          
          <div className="nav-divider"></div>
          
          <button onClick={handleGlobalDunning} className="nav-btn warning">
            ⚡ Run Dunning (All)
          </button>
        </nav>

        <button onClick={handleLogout} className="nav-btn danger">
            🚪 Logout
        </button>
      </aside>

      {/* --- MAIN CONTENT AREA --- */}
      <main className="main-content">
        
        {activeView === 'dashboard' && (
          <>
            <h2 className="page-header">Dashboard Overview</h2>
            
            {/* Stats Grid */}
            <div className="stats-grid">
              <div className="stat-card" style={{ borderLeftColor: '#4299e1' }}>
                <p className="stat-title">Total Customers</p>
                <h3 className="stat-value">{stats.total}</h3>
              </div>
              
              <div className="stat-card" style={{ borderLeftColor: '#48bb78' }}>
                <p className="stat-title">Active Users</p>
                <h3 className="stat-value">{stats.active}</h3>
              </div>
              
              <div className="stat-card" style={{ borderLeftColor: '#f56565' }}>
                <p className="stat-title">Blocked / Risky</p>
                <h3 className="stat-value">{stats.blocked}</h3>
              </div>
              
              <div className="stat-card" style={{ borderLeftColor: '#ed8936' }}>
                <p className="stat-title">Total Overdue</p>
                <h3 className="stat-value">${stats.overdueAmt}</h3>
              </div>
            </div>

            <CustomerList key={refreshTrigger} onRefresh={refreshDashboard} />
          </>
        )}

        {activeView === 'register' && (
          <div>
            <h2 className="page-header">Register New Customer</h2>
            <RegisterCustomer onSuccess={() => { setActiveView('dashboard'); refreshDashboard(); }} />
          </div>
        )}

        {activeView === 'rules' && (
          <div>
            <h2 className="page-header">Dunning Rules Configuration</h2>
            <RuleEngine />
          </div>
        )}

        {activeView === 'simulate' && (
            <div>
                <h2 className="page-header">Simulate Notifications</h2>
                <SimulatePayment />
            </div>
        )}

        {activeView === 'logs' && <DunningLogs />}

        {activeView === 'tickets' && (
            <div>
                <h2 className="page-header">Support Ticket Management</h2>
                <TicketManager />
            </div>
        )}

      </main>
    </div>
  );
};

export default AdminDashboard;
