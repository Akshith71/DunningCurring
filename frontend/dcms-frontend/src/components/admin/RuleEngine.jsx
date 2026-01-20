import React, { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const RuleEngine = () => {
  const [rules, setRules] = useState([]);
  
  // ✅ FIX 1: Set 'active: true' by default
  const [newRule, setNewRule] = useState({ 
    startDay: '', 
    endDay: '', 
    action: 'NOTIFY', 
    channel: 'SMS',
    active: true 
  });

  useEffect(() => {
    fetchRules();
  }, []);

  const fetchRules = async () => {
    try {
      const res = await api.get('/api/admin/rules');
      setRules(res.data);
    } catch (error) {
      console.error("Failed to fetch rules");
    }
  };

  const createRule = async (e) => {
    e.preventDefault();
    try {
      // ✅ FIX 2: Ensure days are integers
      const payload = {
        ...newRule,
        startDay: parseInt(newRule.startDay),
        endDay: parseInt(newRule.endDay)
      };

      await api.post('/api/admin/rules', payload);
      alert("Rule Added Successfully!");
      
      // Reset form and refresh list
      setNewRule({ startDay: '', endDay: '', action: 'NOTIFY', channel: 'SMS', active: true });
      fetchRules();
    } catch(e) { 
        alert("Error creating rule"); 
    }
  };

  const deleteRule = async (id) => {
    if(window.confirm("Delete this rule?")) {
       try {
         await api.delete(`/api/admin/rules/${id}`);
         fetchRules();
       } catch (e) {
         alert("Failed to delete rule");
       }
    }
  };

  return (
    <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '8px' }}>
      <h3>Dunning Configuration Rules</h3>
      
      <table style={{ width: '100%', marginBottom: '30px', borderCollapse: 'collapse' }}>
        <thead style={{ background: '#eee' }}>
          <tr>
              <th style={{padding: '10px', textAlign:'left'}}>Days Range</th>
              <th style={{textAlign:'left'}}>Action</th>
              <th style={{textAlign:'left'}}>Channel</th>
              <th></th>
          </tr>
        </thead>
        <tbody>
          {rules.map(r => (
            <tr key={r.id} style={{ borderBottom: '1px solid #ddd' }}>
              <td style={{ padding: '10px' }}>Day {r.startDay} - {r.endDay}</td>
              <td>{r.action}</td>
              <td>{r.channel}</td>
              <td style={{ textAlign: 'right' }}>
                  <button onClick={() => deleteRule(r.id)} style={{ color: 'red', cursor: 'pointer' }}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <form onSubmit={createRule} style={{ padding: '20px', background: '#f7fafc', borderRadius: '8px', display: 'flex', gap: '10px', alignItems: 'center' }}>
        <input 
            type="number" 
            placeholder="Start Day" 
            value={newRule.startDay}
            onChange={e => setNewRule({...newRule, startDay: e.target.value})} 
            required 
            style={inputStyle}
        />
        <input 
            type="number" 
            placeholder="End Day" 
            value={newRule.endDay}
            onChange={e => setNewRule({...newRule, endDay: e.target.value})} 
            required 
            style={inputStyle}
        />
        
        {/* ✅ FIX 3: Updated values to match Backend Entity comments (NOTIFY, THROTTLE, BLOCK) */}
        <select 
            value={newRule.action} 
            onChange={e => setNewRule({...newRule, action: e.target.value})}
            style={inputStyle}
        >
             <option value="NOTIFY">Notify (SMS/Email)</option>
             <option value="THROTTLE">Throttle Speed</option>
             <option value="BLOCK">Block Service</option>
        </select>
        
        <select 
    value={newRule.action} 
    onChange={e => setNewRule({...newRule, action: e.target.value})}
    style={inputStyle}
>
     <option value="NOTIFY">Notify Only (No Restriction)</option> {/* ✅ Allowed now */}
     <option value="THROTTLE">Throttle Speed</option>
     <option value="BLOCK">Block Service</option>
</select>
        <button type="submit" style={btnStyle}>Add Rule</button>
      </form>
    </div>
  );
};

const inputStyle = { padding: '8px', borderRadius: '4px', border: '1px solid #ccc' };
const btnStyle = { padding: '8px 16px', backgroundColor: '#3182ce', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' };

export default RuleEngine;