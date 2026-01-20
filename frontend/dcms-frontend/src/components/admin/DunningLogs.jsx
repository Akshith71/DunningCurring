import React, { useEffect, useState } from 'react';
import api from '../../api/axiosConfig';

const DunningLogs = () => {
    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchLogs();
    }, []);

    const fetchLogs = async () => {
        try {
            setLoading(true);
            // ✅ MATCHES YOUR CONTROLLER: /api/admin/services + /dunning/logs
            const res = await api.get('/api/admin/services/dunning/logs');
            setLogs(res.data);
        } catch (error) {
            console.error("Error fetching logs", error);
        } finally {
            setLoading(false);
        }
    };

    // Helper for Badge Colors
    const getActionColor = (action) => {
        if (!action) return 'warning';
        if (action === 'BLOCK') return 'danger';
        if (action === 'THROTTLE') return 'warning';
        return 'success'; // For NOTIFY
    };

    if (loading) return <div style={{ padding: '20px', color: '#718096' }}>Loading Logs...</div>;

    return (
        <div>
            {/* Header Section */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h2 className="page-header" style={{ margin: 0 }}>📜 Global Dunning Logs</h2>
                <button 
                    onClick={fetchLogs} 
                    className="nav-btn" 
                    style={{ background: '#e2e8f0', color: '#2d3748', border: '1px solid #cbd5e0' }}
                >
                    🔄 Refresh
                </button>
            </div>

            {/* Table Card */}
            <div className="card full-width" style={{ background: 'white', borderRadius: '12px', padding: '20px', boxShadow: '0 4px 6px rgba(0,0,0,0.05)' }}>
                <div className="table-responsive">
                    {logs.length === 0 ? (
                        <p className="empty-msg" style={{ textAlign: 'center', color: '#a0aec0', padding: '20px' }}>
                            No dunning activity recorded yet.
                        </p>
                    ) : (
                        <table className="data-table" style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                                <tr style={{ background: '#f7fafc', textAlign: 'left' }}>
                                    <th style={{ padding: '12px', borderBottom: '1px solid #e2e8f0', color: '#718096' }}>ID</th>
                                    <th style={{ padding: '12px', borderBottom: '1px solid #e2e8f0', color: '#718096' }}>Customer</th>
                                    <th style={{ padding: '12px', borderBottom: '1px solid #e2e8f0', color: '#718096' }}>Action</th>
                                    <th style={{ padding: '12px', borderBottom: '1px solid #e2e8f0', color: '#718096' }}>Channel</th>
                                    <th style={{ padding: '12px', borderBottom: '1px solid #e2e8f0', color: '#718096' }}>Date & Time</th>
                                    <th style={{ padding: '12px', borderBottom: '1px solid #e2e8f0', color: '#718096' }}>Message</th>
                                </tr>
                            </thead>
                            <tbody>
                                {logs.map((log) => (
                                    <tr key={log.id} style={{ borderBottom: '1px solid #edf2f7' }}>
                                        <td style={{ padding: '12px' }}>#{log.id}</td>
                                        
                                        {/* Customer Name + ID */}
                                        <td style={{ padding: '12px', fontWeight: 'bold', color: '#2d3748' }}>
                                            {log.customer ? log.customer.name : 'Unknown'} 
                                            <span style={{ fontSize: '0.8em', color: '#718096', display: 'block', fontWeight: 'normal' }}>
                                                (ID: {log.customer?.id})
                                            </span>
                                        </td>

                                        {/* Action Badge */}
                                        <td style={{ padding: '12px' }}>
                                            <span className={`status-badge ${getActionColor(log.action)}`} 
                                                  style={{ 
                                                      padding: '4px 8px', 
                                                      borderRadius: '12px', 
                                                      fontSize: '0.85em', 
                                                      fontWeight: 'bold',
                                                      background: log.action === 'BLOCK' ? '#fed7d7' : (log.action === 'THROTTLE' ? '#feebc8' : '#c6f6d5'),
                                                      color: log.action === 'BLOCK' ? '#822727' : (log.action === 'THROTTLE' ? '#744210' : '#22543d')
                                                  }}>
                                                {log.action}
                                            </span>
                                        </td>

                                        <td style={{ padding: '12px' }}>{log.channel || 'SYSTEM'}</td>
                                        
                                        {/* Formatted Date */}
                                        <td style={{ padding: '12px' }}>
                                            {log.actionDate ? new Date(log.actionDate).toLocaleString() : 'N/A'}
                                        </td>

                                        <td style={{ padding: '12px', maxWidth: '300px', fontSize: '0.9em', color: '#4a5568' }}>
                                            {log.message}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </div>
    );
};

export default DunningLogs;