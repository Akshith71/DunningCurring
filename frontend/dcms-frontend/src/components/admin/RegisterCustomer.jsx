import React, { useState } from 'react';
import api from '../../api/axiosConfig';

const RegisterCustomer = ({ onSuccess }) => {
  const [step, setStep] = useState(1);
  const [loading, setLoading] = useState(false);
  const [registeredId, setRegisteredId] = useState(null);

  // Error State for Validation
  const [errors, setErrors] = useState({});

  // Form States
  const [userForm, setUserForm] = useState({
    username: '', password: '', name: '', email: '', mobile: '', customerType: 'POSTPAID'
  });
  
  const [serviceForm, setServiceForm] = useState({
    serviceType: 'MOBILE', identifier: ''
  });

  // ✅ VALIDATION LOGIC
  const validateStep1 = () => {
    let tempErrors = {};
    let isValid = true;

    // Email Validation (Regex)
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!userForm.email) {
      tempErrors.email = "Email is required";
      isValid = false;
    } else if (!emailRegex.test(userForm.email)) {
      tempErrors.email = "Invalid email format (e.g., user@mail.com)";
      isValid = false;
    }

    // Mobile Validation (Exactly 10 Digits)
    const mobileRegex = /^[0-9]{10}$/;
    if (!userForm.mobile) {
      tempErrors.mobile = "Mobile number is required";
      isValid = false;
    } else if (!mobileRegex.test(userForm.mobile)) {
      tempErrors.mobile = "Mobile must be exactly 10 digits";
      isValid = false;
    }

    setErrors(tempErrors);
    return isValid;
  };

  // STEP 1: Register User
  const handleRegister = async (e) => {
    e.preventDefault();

    // 🛑 Run Validation Check
    if (!validateStep1()) return;

    setLoading(true);
    try {
      // 1. Register the user
      await api.post('/auth/register', userForm);
      
      // 2. Fetch the new customer's ID to link service
      const res = await api.get('/api/admin/customers/customers');
      const newUser = res.data.find(u => u.email === userForm.email); 

      if (newUser) {
        setRegisteredId(newUser.id);
        setStep(2); // Move to Service Step
      } else {
        alert("User registered, but could not retrieve ID automatically. Please add service manually.");
        onSuccess();
      }
    } catch (err) {
      alert("Registration Failed: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  // STEP 2: Add Service
  const handleAddService = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.post('/api/admin/services', {
        customerId: registeredId,
        serviceType: serviceForm.serviceType,
        identifier: serviceForm.identifier
      });
      alert("✅ Success! Customer and Service created.");
      onSuccess(); // Return to Dashboard
    } catch (err) {
      alert("User created, but failed to add Service.");
    } finally {
      setLoading(false);
    }
  };

  // Helper to clear error when user types
  const handleUserChange = (e) => {
      const { name, value } = e.target;
      setUserForm({ ...userForm, [name]: value });
      // Clear specific error if exists
      if (errors[name]) setErrors({ ...errors, [name]: null });
  };

  return (
    <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '500px' }}>
      {step === 1 ? (
        <form onSubmit={handleRegister}>
          <h3>Step 1: Customer Details</h3>
          
          <input 
            name="username" 
            placeholder="Username" 
            required 
            onChange={handleUserChange} 
            style={inputStyle} 
          />
          
          <input 
            name="password" 
            placeholder="Password" 
            type="password" 
            required 
            onChange={handleUserChange} 
            style={inputStyle} 
          />
          
          <input 
            name="name" 
            placeholder="Full Name" 
            required 
            onChange={handleUserChange} 
            style={inputStyle} 
          />
          
          {/* Email Field with Error */}
          <div>
            <input 
                name="email" 
                placeholder="Email" 
                type="email" 
                required 
                onChange={handleUserChange} 
                style={{...inputStyle, borderColor: errors.email ? 'red' : '#ddd'}} 
            />
            {errors.email && <div style={errorStyle}>{errors.email}</div>}
          </div>

          {/* Mobile Field with Error */}
          <div>
            <input 
                name="mobile" 
                placeholder="Mobile (10 digits)" 
                required 
                maxLength="10"
                onChange={handleUserChange} 
                style={{...inputStyle, borderColor: errors.mobile ? 'red' : '#ddd'}} 
            />
            {errors.mobile && <div style={errorStyle}>{errors.mobile}</div>}
          </div>

          <select name="customerType" onChange={handleUserChange} style={inputStyle}>
            <option value="POSTPAID">Postpaid</option>
            <option value="PREPAID">Prepaid</option>
          </select>
          
          <button type="submit" disabled={loading} style={btnStyle}>
            {loading ? 'Processing...' : 'Next: Add Service'}
          </button>
        </form>
      ) : (
        <form onSubmit={handleAddService}>
          <h3>Step 2: Add Service for {userForm.name}</h3>
          <select onChange={e => setServiceForm({...serviceForm, serviceType: e.target.value})} style={inputStyle}>
            <option value="MOBILE">Mobile</option>
            <option value="BROADBAND">Broadband</option>
          </select>
          <input 
            placeholder={serviceForm.serviceType === 'MOBILE' ? "Phone Number" : "Fiber ID"} 
            required 
            onChange={e => setServiceForm({...serviceForm, identifier: e.target.value})} 
            style={inputStyle} 
          />
          <button type="submit" disabled={loading} style={btnStyle}>Complete Registration</button>
          <button type="button" onClick={onSuccess} style={{...btnStyle, background: '#ccc', marginTop:'10px'}}>Skip</button>
        </form>
      )}
    </div>
  );
};

const inputStyle = { width: '100%', padding: '10px', marginBottom: '10px', border: '1px solid #ddd', borderRadius: '4px', boxSizing: 'border-box' };
const btnStyle = { width: '100%', padding: '12px', background: '#3182ce', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight:'bold' };
const errorStyle = { color: 'red', fontSize: '0.85rem', marginBottom: '10px', marginTop: '-5px' };

export default RegisterCustomer;