import React, { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const CustomerList = ({ onRefresh }) => {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [details, setDetails] = useState({ services: [], payments: [], logs: [] });

  useEffect(() => { loadCustomers(); }, []);

  useEffect(() => {
    const term = searchTerm.toLowerCase();
    setFilteredCustomers(customers.filter(c => 
      c.name.toLowerCase().includes(term) || 
      c.id.toString().includes(term)
    ));
  }, [searchTerm, customers]);

  const loadCustomers = async () => {
    try {
      // Added timestamp to prevent caching
      const res = await api.get(`/api/admin/customers/customers?_=${new Date().getTime()}`);
      setCustomers(res.data);
      setFilteredCustomers(res.data);
    } catch (e) { console.error(e); }
  };

  const selectCustomer = async (id) => {
    setSelectedCustomer(id);
    try {
      // Added timestamp to prevent caching
      const ts = new Date().getTime();
      const [srv, pay, logs] = await Promise.all([
        api.get(`/api/admin/customers/${id}/services?_=${ts}`),
        api.get(`/api/admin/customers/${id}/payments?_=${ts}`),
        api.get(`/api/admin/customers/${id}/logs?_=${ts}`)
      ]);
      setDetails({ services: srv.data, payments: pay.data, logs: logs.data });
    } catch (e) { console.error(e); }
  };

  // --- ACTIONS ---

  const handleForceBlockCustomer = async () => {
    if(!selectedCustomer) return;
    const activeServices = details.services.filter(s => s.status === 'ACTIVE');
    if (activeServices.length === 0) return alert("Customer is already fully blocked.");

    if(window.confirm(`🚨 FORCE BLOCK CUSTOMER #${selectedCustomer}?\n\nThis will immediately BLOCK ${activeServices.length} active services.`)) {
        try {
            for (const service of activeServices) {
                await api.put(`/api/admin/services/${service.id}/status`, null, { params: { status: 'BLOCKED' } });
            }
            alert("Customer has been FORCE BLOCKED.");
            
            // ✅ REFRESH EVERYTHING
            await selectCustomer(selectedCustomer); // Update Right Panel
            await loadCustomers();                  // Update Left List (Red Badge)
            if (onRefresh) onRefresh();             // Update Parent Dashboard Stats
        } catch (e) {
            alert("Error blocking customer services.");
        }
    }
  };

  const changeServiceStatus = async (serviceId, newStatus) => {
      // Optimistic Update: Confirm first
      if(window.confirm(`Change status to ${newStatus}?`)) {
          try {
              await api.put(`/api/admin/services/${serviceId}/status`, null, { params: { status: newStatus } });
              
              // ✅ REFRESH EVERYTHING
              await selectCustomer(selectedCustomer); // Update Right Panel (Button Color)
              await loadCustomers();                  // Update Left List (Status Badge)
              if (onRefresh) onRefresh();             // Update Parent Dashboard Stats
          } catch (e) {
              alert("Failed to update status.");
          }
      }
  };

  const handleManualDunning = async () => {
    if(window.confirm(`Trigger Dunning for #${selectedCustomer}?`)) {
        await api.post(`/api/admin/dunning/apply/${selectedCustomer}`);
        alert("Dunning Triggered.");
        await selectCustomer(selectedCustomer);
        await loadCustomers(); // Update List
        if (onRefresh) onRefresh();
    }
  };

  const handleManualCure = async () => {
    const customer = customers.find(c => c.id === selectedCustomer);
    if(window.confirm(`Simulate Payment of $${customer?.overdueAmount}?`)) {
        await api.post('/api/payment/simulate', null, { params: { customerId: selectedCustomer, amount: customer?.overdueAmount }});
        alert("Payment Simulated.");
        await selectCustomer(selectedCustomer);
        await loadCustomers(); // Update List
        if (onRefresh) onRefresh();
    }
  };

  return (
    <div style={{ display: 'flex', gap: '20px', height: '80vh' }}>
      
      {/* LEFT: List */}
      <div style={{ flex: 1, background: 'white', padding: '20px', borderRadius: '8px', overflowY: 'auto', border: '1px solid #e2e8f0' }}>
        <input placeholder="Search..." value={searchTerm} onChange={e => setSearchTerm(e.target.value)} style={{width:'100%', padding:'10px', marginBottom:'15px', border:'1px solid #ccc'}} />
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead><tr style={{textAlign:'left', borderBottom:'2px solid #eee'}}><th>ID</th><th>Name</th><th>Status</th><th></th></tr></thead>
          <tbody>
            {filteredCustomers.map(c => (
              <tr key={c.id} style={{borderBottom:'1px solid #f7fafc'}}>
                <td style={{padding:'10px 0'}}>{c.id}</td>
                <td>{c.name}<br/><small style={{color:'#718096'}}>{c.email}</small></td>
                <td>
                   <span style={{ 
                      padding: '3px 8px', borderRadius: '4px', fontSize: '0.8em', fontWeight: 'bold',
                      background: c.status === 'BLOCKED' ? '#e53e3e' : (c.overdueDays > 0 ? '#fff5f5' : '#f0fff4'),
                      color: c.status === 'BLOCKED' ? 'white' : (c.overdueDays > 0 ? 'red' : 'green')
                   }}>
                      {c.status === 'BLOCKED' ? 'BLOCKED' : (c.overdueDays > 0 ? `${c.overdueDays} Days OD` : 'Active')}
                   </span>
                </td>
                <td><button onClick={() => selectCustomer(c.id)} style={{color:'#3182ce', border:'none', background:'none', cursor:'pointer'}}>View</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* RIGHT: Details Panel */}
      {selectedCustomer && (
        <div style={{ flex: 1.5, background: 'white', padding: '20px', borderRadius: '8px', overflowY: 'auto', border: '1px solid #e2e8f0' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px', borderBottom: '1px solid #eee', paddingBottom: '15px' }}>
            <h3 style={{margin:0}}>Customer #{selectedCustomer}</h3>
            <div style={{display:'flex', gap:'5px'}}>
                <button onClick={handleForceBlockCustomer} style={{background:'#c53030', color:'white', border:'none', padding:'8px 12px', borderRadius:'4px', cursor:'pointer', fontWeight:'bold'}}>
                    🛑 Force Block
                </button>
                <button onClick={handleManualDunning} style={{background:'#dd6b20', color:'white', border:'none', padding:'8px 12px', borderRadius:'4px', cursor:'pointer'}}>⚠️ Dunning</button>
                <button onClick={handleManualCure} style={{background:'#48bb78', color:'white', border:'none', padding:'8px 12px', borderRadius:'4px', cursor:'pointer'}}>⚡ Cure</button>
            </div>
          </div>

          <h4>Services</h4>
          <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '20px' }}>
            {details.services.map(s => (
               <div key={s.id} style={{ border: '1px solid #ddd', padding: '15px', borderRadius: '6px', width: '200px', background: s.status === 'ACTIVE' ? '#f0fff4' : '#fff5f5' }}>
                 <strong>{s.serviceType}</strong>
                 <div style={{marginBottom:'10px', fontSize:'0.9em'}}>Status: <b style={{color: s.status==='ACTIVE'?'green':'red'}}>{s.status}</b></div>
                 <div style={{display:'flex', gap:'5px'}}>
                    {/* ✅ CASE INSENSITIVE CHECK */}
                    {(s.status || '').toUpperCase() === 'ACTIVE' ? (
                        <button onClick={() => changeServiceStatus(s.id, 'BLOCKED')} style={{flex:1, background:'#e53e3e', color:'white', border:'none', padding:'5px', cursor:'pointer'}}>🛑 Block</button>
                    ) : (
                        <button onClick={() => changeServiceStatus(s.id, 'ACTIVE')} style={{flex:1, background:'#38a169', color:'white', border:'none', padding:'5px', cursor:'pointer'}}>✅ Activate</button>
                    )}
                 </div>
               </div>
            ))}
          </div>

           <h4>Payment History</h4>
           <table style={{ width: '100%', fontSize: '0.9em', borderCollapse: 'collapse', marginBottom: '20px' }}>
            <thead><tr style={{background: '#f7fafc', textAlign:'left'}}><th style={{padding:'8px'}}>Date</th><th>Amount</th><th>Status</th></tr></thead>
            <tbody>
                {details.payments.length === 0 ? <tr><td colSpan="3" style={{padding:'8px'}}>No payments</td></tr> : 
                 details.payments.map((p, i) => (
                    <tr key={i} style={{ borderBottom: '1px solid #eee' }}>
                        <td style={{padding:'8px'}}>{p.paymentDate}</td><td>${p.amount}</td><td style={{color:'green'}}>{p.status}</td>
                    </tr>
                ))}
            </tbody>
          </table>

          <h4>Logs</h4>
          <ul style={{background:'#f7fafc', padding:'10px', maxHeight:'150px', overflowY:'auto'}}>
              {details.logs.map((l,i) => <li key={i} style={{fontSize:'0.8em', borderBottom:'1px solid #eee'}}>{l.date}: {l.action}</li>)}
          </ul>
        </div>
      )}
    </div>
  );
};

export default CustomerList;