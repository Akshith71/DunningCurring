import React, { useState, useEffect, useRef } from 'react';
import api from '../../api/axiosConfig';

const SimulatePayment = () => {
    // --- State Management ---
    const [selectedCustomerId, setSelectedCustomerId] = useState('');
    const [action, setAction] = useState('PAYMENT_REMINDER'); 
    const [message, setMessage] = useState('Dear customer, your payment is due.');
    const [loading, setLoading] = useState(false);

    // Search & Dropdown State
    const [customers, setCustomers] = useState([]); 
    const [searchTerm, setSearchTerm] = useState(''); 
    const [showDropdown, setShowDropdown] = useState(false); 
    
    // Ref for detecting clicks outside
    const wrapperRef = useRef(null);

    // --- 1. Fetch Customers on Load ---
    useEffect(() => {
        const fetchCustomers = async () => {
            try {
                // Fetch list for the dropdown
                const res = await api.get('/api/admin/customers/customers');
                setCustomers(res.data);
            } catch (error) {
                console.error("Error fetching customers list", error);
            }
        };
        fetchCustomers();

        // Close dropdown if clicked outside
        const handleClickOutside = (event) => {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
                setShowDropdown(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    // --- 2. Filter Logic ---
    const filteredCustomers = customers.filter(c => 
        (c.name && c.name.toLowerCase().includes(searchTerm.toLowerCase())) || 
        (c.id && c.id.toString().includes(searchTerm))
    );

    // --- 3. Handle Selection ---
    const selectCustomer = (customer) => {
        setSelectedCustomerId(customer.id);
        setSearchTerm(`${customer.name} (ID: ${customer.id})`); // Display Name in Input
        setShowDropdown(false);
    };

    // --- 4. Submit Simulation ---
    const handleSimulation = async (e) => {
        e.preventDefault();
        if (!selectedCustomerId) return alert("Please Select a Customer");

        setLoading(true);
        try {
            // ✅ CORRECT URL matching AdminServiceController
            await api.post('/api/admin/services/simulate/notification', null, {
                params: {
                    customerId: selectedCustomerId,
                    action: action,
                    message: message
                }
            });
            alert(`✅ Notification sent to Customer #${selectedCustomerId}`);
        } catch (error) {
            console.error("Simulation failed", error);
            const errMsg = error.response?.data?.message || "Failed to simulate notification";
            alert(`❌ ${errMsg}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="card full-width" style={{ maxWidth: '600px', paddingBottom:'150px' }}> 
            <h3 style={{ borderBottom: '1px solid #eee', paddingBottom: '10px' }}>📨 Trigger Notification</h3>
            
            <form onSubmit={handleSimulation} style={{ display: 'flex', flexDirection: 'column', gap: '15px', marginTop: '15px' }}>
                
                {/* SEARCHABLE INPUT FIELD */}
                <div style={{ position: 'relative' }} ref={wrapperRef}>
                    <label style={{ display: 'block', marginBottom: '5px', color: '#718096' }}>Target Customer</label>
                    
                    <input 
                        type="text" 
                        className="form-input" 
                        value={searchTerm}
                        onChange={(e) => {
                            setSearchTerm(e.target.value);
                            setSelectedCustomerId(''); // Reset ID if typing manually
                            setShowDropdown(true);
                        }}
                        onFocus={() => setShowDropdown(true)}
                        placeholder="Search by Name or ID..."
                        style={{ padding: '10px', width: '100%', borderRadius: '6px', border: '1px solid #cbd5e0' }}
                        autoComplete="off"
                    />

                    {/* CUSTOMER DROPDOWN */}
                    {showDropdown && (
                        <div style={{
                            position: 'absolute',
                            top: '100%',
                            left: 0,
                            right: 0,
                            background: 'white',
                            border: '1px solid #cbd5e0',
                            borderRadius: '6px',
                            maxHeight: '200px',
                            overflowY: 'auto',
                            zIndex: 1000,
                            boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
                            marginTop: '5px'
                        }}>
                            {filteredCustomers.length === 0 ? (
                                <div style={{ padding: '10px', color: '#a0aec0', fontStyle:'italic' }}>No customers found.</div>
                            ) : (
                                filteredCustomers.map(c => (
                                    <div 
                                        key={c.id}
                                        onClick={() => selectCustomer(c)}
                                        style={{
                                            padding: '10px',
                                            cursor: 'pointer',
                                            borderBottom: '1px solid #edf2f7',
                                            transition: 'background 0.1s'
                                        }}
                                        onMouseEnter={(e) => e.target.style.background = '#f7fafc'}
                                        onMouseLeave={(e) => e.target.style.background = 'white'}
                                    >
                                        <span style={{fontWeight:'bold', color:'#2d3748'}}>{c.name}</span>
                                        <span style={{ fontSize: '0.85em', color: '#718096', marginLeft: '8px' }}>
                                            (ID: {c.id})
                                        </span>
                                    </div>
                                ))
                            )}
                        </div>
                    )}
                </div>

                {/* NOTIFICATION TYPE */}
                <div>
                    <label style={{ display: 'block', marginBottom: '5px', color: '#718096' }}>Notification Type</label>
                    <select 
                        className="form-input"
                        value={action}
                        onChange={(e) => setAction(e.target.value)}
                        style={{ padding: '10px', width: '100%', borderRadius: '6px', border: '1px solid #cbd5e0', background: 'white' }}
                    >
                        <option value="PAYMENT_REMINDER">📢 Payment Reminder</option>
                        <option value="WARNING">⚠️ Service Warning</option>
                        <option value="BLOCK">🚫 Service Blocked</option>
                        <option value="SUCCESS">✅ Payment Received (Success)</option>
                    </select>
                </div>

                {/* MESSAGE BOX */}
                <div>
                    <label style={{ display: 'block', marginBottom: '5px', color: '#718096' }}>Message Content</label>
                    <textarea 
                        className="form-input"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        rows="3"
                        style={{ padding: '10px', width: '100%', borderRadius: '6px', border: '1px solid #cbd5e0', fontFamily: 'inherit' }}
                    />
                </div>

                {/* SUBMIT BUTTON */}
                <button 
                    type="submit" 
                    disabled={loading || !selectedCustomerId}
                    style={{ 
                        padding: '12px', 
                        background: loading || !selectedCustomerId ? '#a0aec0' : '#3182ce', 
                        color: 'white', 
                        border: 'none', 
                        borderRadius: '6px', 
                        cursor: loading || !selectedCustomerId ? 'not-allowed' : 'pointer',
                        fontWeight: 'bold',
                        fontSize: '1rem',
                        transition: 'background 0.2s',
                        marginTop: '10px'
                    }}
                >
                    {loading ? 'Sending...' : '🚀 Send Notification'}
                </button>
            </form>
        </div>
    );
};

export default SimulatePayment;