import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import { AuthContext } from '../context/AuthContext';
import './CustomerDashboard.css';
import ChatWidget from '../components/common/ChatWidget'; // Chat Bot Integration

const CustomerDashboard = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  // --- State Management ---
  const [profile, setProfile] = useState(null);
  const [services, setServices] = useState([]);
  const [payments, setPayments] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  // UI State
  const [showNotifModal, setShowNotifModal] = useState(false);
  
  // Payment Modal State
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [paymentAmount, setPaymentAmount] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('UPI'); 
  
  // Add Service Modal State
  const [showAddServiceModal, setShowAddServiceModal] = useState(false);
  const [serviceCategory, setServiceCategory] = useState('BROADBAND');
  const [mobilePlanType, setMobilePlanType] = useState('PREPAID');

  // --- Effect 1: Initial Load & Auth Check ---
  useEffect(() => {
    if (!user) {
      const storedUser = localStorage.getItem('user');
      if (!storedUser) {
        navigate('/login');
        return;
      }
    }
    fetchDashboardData();
  }, [user, navigate]);

  // --- Effect 2: ✅ Auto-Refresh Data (Every 5 Seconds) ---
  useEffect(() => {
    const intervalId = setInterval(() => {
        // Silently refresh data without showing full loading spinner
        fetchDashboardData(true); 
    }, 5000); 

    return () => clearInterval(intervalId); // Cleanup on unmount
  }, [user]);

  // --- API Handlers ---
  const fetchDashboardData = async (silent = false) => {
    try {
      if (!silent) setLoading(true);
      
      // robust ID retrieval
      let userId = user?.id || user?.customerId;
      if (!userId) {
         const stored = JSON.parse(localStorage.getItem('user'));
         userId = stored?.id || stored?.customerId;
      }

      // Parallel Fetch
      const [profileRes, srvRes, payRes, logsRes, historyRes] = await Promise.all([
        api.get('/api/customer/profile'),
        api.get('/api/customer/services'),
        api.get('/api/customer/payments'), 
        userId 
          ? api.get(`/api/customer/${userId}/notifications`).catch(() => ({ data: [] })) 
          : Promise.resolve({ data: [] }),
        api.get('/api/payment/history').catch(() => ({ data: [] })) 
      ]);

      setProfile(profileRes.data);
      setServices(srvRes.data);
      
      // Prefer detailed history
      setPayments(historyRes.data.length > 0 ? historyRes.data : payRes.data); 

      // ✅ FIX: Sort Notifications (Newest First)
      if (logsRes.data && logsRes.data.length > 0) {
        const sortedNotifs = logsRes.data.sort((a, b) => 
            new Date(b.actionDate) - new Date(a.actionDate)
        );
        setNotifications(sortedNotifs);
      } else {
        setNotifications([]);
      }

    } catch (error) {
      console.error("Error loading dashboard", error);
    } finally {
      if (!silent) setLoading(false);
    }
  };

  // Initiate Payment
  const handleInitiatePayment = async (e) => {
    e.preventDefault();
    if (!paymentAmount || parseFloat(paymentAmount) <= 0) return alert("Please enter a valid amount");

    let targetId = user?.id || user?.customerId || profile?.id;

    try {
      const res = await api.post('/api/payment/initiate', null, { 
        params: { 
            customerId: targetId, 
            amount: paymentAmount,
            paymentMethod: paymentMethod 
        } 
      });
      
      alert(`📧 ${res.data}`); 
      setShowPaymentModal(false);
      setPaymentAmount('');
      fetchDashboardData(); 
    } catch (error) {
      console.error("Payment Init Failed", error);
      alert("❌ Failed to initiate payment: " + (error.response?.data || "Server Error"));
    }
  };

  // Add Service Logic
  const handleAddService = async (e) => {
    e.preventDefault();
    let targetId = user?.id || user?.customerId || profile?.id;
    let finalServiceType = serviceCategory === 'MOBILE' ? `MOBILE_${mobilePlanType}` : serviceCategory;

    try {
        await api.post(`/api/customer/${targetId}/services`, null, { params: { serviceType: finalServiceType } });
        alert(`✅ New Service (${finalServiceType}) Added!`);
        setShowAddServiceModal(false);
        fetchDashboardData();
    } catch (error) {
        const msg = error.response?.data || "Failed to add service.";
        alert(`❌ ${msg}`);
    }
  };

  // Helpers
  const formatDate = (dateData) => {
    if (!dateData) return 'Just Now';
    // Handle Java array format [yyyy, mm, dd, hh, mm]
    if (Array.isArray(dateData)) {
        return new Date(dateData[0], dateData[1]-1, dateData[2], dateData[3], dateData[4]).toLocaleString();
    }
    return new Date(dateData).toLocaleString();
  };

  const getStatusClass = (status) => {
    if (!status) return 'warning';
    const s = status.toUpperCase();
    if (s === 'SUCCESS' || s === 'ACTIVE') return 'success'; 
    if (s === 'PENDING') return 'warning'; 
    return 'danger'; 
  };

  if (loading) return <div className="loader-container"><div className="loader"></div><p>Loading Dashboard...</p></div>;

  return (
    <div className="dashboard-container">
      
      {/* HEADER */}
      <header className="dashboard-header">
        <div className="header-left">
          <h2>Welcome, {profile?.name || user?.username} 👋</h2>
          <span className="customer-id">ID: #{profile?.id || user?.id}</span>
        </div>
        <div className="header-actions">
          {/* Bell Icon */}
          <button className="icon-btn" onClick={() => setShowNotifModal(true)}>
            🔔 {notifications.length > 0 && <span className="badge">{notifications.length}</span>}
          </button>
          
          <button onClick={() => navigate('/support')} className="icon-btn" style={{background:'#feebc8', color:'#744210', width:'auto', padding:'0 15px'}}>
             🎫 Support
          </button>

          <button onClick={() => setShowPaymentModal(true)} className="icon-btn" style={{background:'#4299e1', color:'white', width:'auto', padding:'0 15px'}}>
            💳 Pay Bill
          </button>

          <button onClick={() => setShowAddServiceModal(true)} className="icon-btn" style={{background:'#48bb78', color:'white', width:'auto', padding:'0 15px'}}>
            ➕ Service
          </button>

          <button onClick={logout} className="logout-btn">Logout</button>
        </div>
      </header>

      {/* MAIN CONTENT */}
      <div className="main-content">
        
        {/* Top Row: Profile & Services */}
        <div className="dashboard-top-row">
          <div className="card profile-card">
            <h3>👤 My Profile</h3>
            <div className="profile-details">
              <div className="detail-row"><span className="label">Name</span><span className="value"><strong>{profile?.name}</strong></span></div>
              <div className="detail-row"><span className="label">Email</span><span className="value">{profile?.email}</span></div>
              <div className="detail-row"><span className="label">Status</span><span className={`status-badge ${profile?.overdueDays > 0 ? 'danger' : 'success'}`}>{profile?.overdueDays > 0 ? 'Overdue' : 'Active'}</span></div>
            </div>
            {profile?.overdueAmount > 0 ? (
              <div className="due-alert"><p>⚠️ Due Amount</p><h2>${profile.overdueAmount}</h2></div>
            ) : <div className="no-due-alert">✅ No Pending Dues</div>}
          </div>

          <div className="card services-card">
            <h3>🌐 Active Services</h3>
            {services.length === 0 ? <p className="empty-msg">No services.</p> : (
              <div className="services-list">
                {services.map(s => (
                  <div key={s.id} className="service-row">
                    <div className="service-info"><h4>{s.serviceType}</h4><span className={`status-badge ${getStatusClass(s.status)}`}>{s.status}</span></div>
                    {s.status !== 'ACTIVE' && <button className="pay-btn-small" onClick={() => setShowPaymentModal(true)}>Pay Now</button>}
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* ✅ MIDDLE ROW: NOTIFICATIONS CARD */}
        {notifications.length > 0 && (
          <div className="dashboard-row" style={{ marginTop: '20px' }}>
            <div className="card full-width notification-card">
              <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:'15px'}}>
                  <h3>🔔 Recent Alerts</h3>
                  <button onClick={() => fetchDashboardData(false)} style={{fontSize:'0.8rem', cursor:'pointer', padding:'4px 8px'}}>🔄 Refresh</button>
              </div>
              <div className="notification-list-embedded">
                {notifications.slice(0, 3).map((notif) => ( 
                  <div key={notif.id} className="notification-item-embedded">
                     <div className="notif-content">
                        <strong style={{color: notif.action === 'PAYMENT_REMINDER' ? '#e53e3e' : '#2b6cb0'}}>
                            {notif.action.replace('_', ' ')}
                        </strong>
                        <span style={{marginLeft:'10px', color:'#555'}}>{notif.message}</span>
                     </div>
                     <span className="notif-date-small">{formatDate(notif.actionDate)}</span>
                  </div>
                ))}
                {notifications.length > 3 && (
                    <button className="view-more-btn" onClick={() => setShowNotifModal(true)}>View All ({notifications.length})</button>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Payment History Section */}
        <div className="dashboard-bottom-row">
          <div className="card full-width">
            <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                <h3>💳 Payment History</h3>
                <button onClick={() => fetchDashboardData(false)} style={{padding:'5px 10px', cursor:'pointer'}}>🔄 Refresh Status</button>
            </div>
            <div className="table-responsive">
              {payments.length === 0 ? <p className="empty-msg">No history found.</p> : (
                <table className="data-table">
                  <thead><tr><th>Date</th><th>Method</th><th>Amount</th><th>Status</th></tr></thead>
                  <tbody>
                    {payments.map((p, i) => (
                      <tr key={i}>
                        <td>{formatDate(p.paymentDate)}</td>
                        <td style={{fontWeight:'bold', color:'#555'}}>{p.paymentMethod ? p.paymentMethod.replace('_', ' ') : '-'}</td>
                        <td>${p.amount}</td>
                        <td>
                          <span className={`status-badge ${getStatusClass(p.status)}`}>
                            {p.status === 'PENDING' ? '⏳ Check Email' : p.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* --- MODALS --- */}

      {/* Payment Modal */}
      {showPaymentModal && (
        <div className="modal-overlay" onClick={() => setShowPaymentModal(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()} style={{maxWidth:'400px'}}>
            <div className="modal-header"><h3>💳 Secure Payment</h3><button className="close-btn" onClick={() => setShowPaymentModal(false)}>&times;</button></div>
            <form onSubmit={handleInitiatePayment} style={{display:'flex', flexDirection:'column', gap:'15px', padding:'15px 0'}}>
                <div>
                    <label style={{fontWeight:'bold', display:'block', marginBottom:'5px'}}>Amount ($)</label>
                    <input type="number" value={paymentAmount} onChange={e => setPaymentAmount(e.target.value)} className="form-input" required />
                </div>
                <div>
                    <label style={{fontWeight:'bold', display:'block', marginBottom:'5px'}}>Payment Method</label>
                    <select value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)} className="form-input" style={{width:'100%', padding:'10px', background:'white'}}>
                        <option value="UPI">📱 UPI (GPay / PhonePe)</option>
                        <option value="DEBIT_CARD">💳 Debit Card</option>
                        <option value="CREDIT_CARD">💳 Credit Card</option>
                        <option value="NET_BANKING">🏦 Net Banking</option>
                    </select>
                </div>
                <div style={{background:'#f0fff4', padding:'10px', borderRadius:'6px', fontSize:'0.85em', color:'#2f855a'}}>
                    ℹ️ Verification link will be sent to: <strong>{profile?.email}</strong>
                </div>
                <button type="submit" style={{padding:'12px', background:'#3182ce', color:'white', border:'none', borderRadius:'6px', cursor:'pointer', fontWeight:'bold'}}>
                    🚀 Send Verification Link
                </button>
            </form>
          </div>
        </div>
      )}

      {/* Notification Modal (Full List) */}
      {showNotifModal && (
        <div className="modal-overlay" onClick={() => setShowNotifModal(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header"><h3>🔔 All Notifications</h3><button className="close-btn" onClick={() => setShowNotifModal(false)}>&times;</button></div>
            <div className="notif-list">
                {notifications.map((n, i) => (
                    <div key={i} className="notif-item">
                        <span className="notif-date">{formatDate(n.actionDate)}</span>
                        <div style={{fontWeight:'bold', marginBottom:'4px', color: n.action==='PAYMENT_REMINDER'?'#e53e3e':'#3182ce'}}>
                            {n.action.replace('_', ' ')}
                        </div>
                        <p>{n.message}</p>
                    </div>
                ))}
            </div>
          </div>
        </div>
      )}
      
      {/* Add Service Modal */}
      {showAddServiceModal && (
        <div className="modal-overlay" onClick={() => setShowAddServiceModal(false)}>
          <div className="modal-content" style={{maxWidth:'400px'}} onClick={e => e.stopPropagation()}>
            <h3>➕ Add Service</h3>
            <form onSubmit={handleAddService} style={{display:'flex', flexDirection:'column', gap:'10px', marginTop:'15px'}}>
                <select value={serviceCategory} onChange={e => setServiceCategory(e.target.value)} className="form-input">
                    <option value="BROADBAND">Broadband</option><option value="MOBILE">Mobile</option>
                </select>
                {serviceCategory === 'MOBILE' && <select value={mobilePlanType} onChange={e => setMobilePlanType(e.target.value)} className="form-input"><option value="PREPAID">Prepaid</option><option value="POSTPAID">Postpaid</option></select>}
                <button type="submit" className="nav-btn" style={{background:'#48bb78', color:'white'}}>Add</button>
            </form>
          </div>
        </div>
      )}

      {/* Chat Bot */}
      <ChatWidget customerId={profile?.id || user?.id} customerName={profile?.name} />
    </div>
  );
};

export default CustomerDashboard;