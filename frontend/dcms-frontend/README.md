# DCMS Frontend

React + Vite web application for Dunning & Currying Management System. This frontend provides user interfaces for both admin and customer dashboards with real-time notifications and AI-powered chatbot support.

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Running the Application](#running-the-application)
- [Available Scripts](#available-scripts)
- [Features](#features)
- [Components Guide](#components-guide)
- [API Integration](#api-integration)
- [Authentication](#authentication)
- [Styling](#styling)
- [Troubleshooting](#troubleshooting)

## 📌 Overview

The DCMS Frontend is a modern React application built with Vite that provides:

- **Admin Dashboard**: System monitoring, customer management, and dunning rule configuration
- **Customer Dashboard**: View account status, payments, and billing information
- **Authentication**: Secure JWT-based login and user management
- **Real-Time Notifications**: In-app notification system with email integration
- **AI Chatbot**: Gemini-powered customer support chat widget
- **Payment Tracking**: Monitor payment status and transaction history
- **Responsive Design**: Mobile-friendly user interface
- **Protected Routes**: Role-based access control for secure pages

## 💻 Tech Stack

- **React 19.2.0**: Modern UI library with hooks
- **Vite 7.2.4**: Next-generation build tool and dev server
- **Axios 1.13.2**: HTTP client for API communication
- **React Router DOM 7.11.0**: Client-side routing
- **JWT Decode 4.0.0**: JWT token parsing and validation
- **ESLint 9.39.1**: Code quality and linting
- **Node.js**: JavaScript runtime

### Development Tools
- Vite development server with HMR (Hot Module Replacement)
- ESLint for code quality
- Babel/SWC for JavaScript transformation

## 📁 Project Structure

```
dcms-frontend/
├── src/
│   ├── components/
│   │   ├── admin/
│   │   │   ├── CustomerList.jsx              # Display and manage customers
│   │   │   ├── DunningLogs.jsx               # View dunning activity logs
│   │   │   ├── RegisterCustomer.jsx          # Register new customers
│   │   │   ├── RuleEngine.jsx                # Configure dunning rules
│   │   │   ├── SimulatePayment.jsx           # Test dunning scenarios
│   │   │   └── TicketManager.jsx             # Support ticket management
│   │   │
│   │   ├── common/
│   │   │   ├── ChatWidget.jsx                # AI chatbot interface
│   │   │   └── ChatWidget.css                # Chatbot styling
│   │   │
│   │   └── ProtectedRoute.jsx                # Route protection wrapper
│   │
│   ├── pages/
│   │   ├── AdminDashboard.jsx                # Main admin interface
│   │   ├── AdminDashboard.css                # Admin dashboard styling
│   │   ├── CustomerDashboard.jsx             # Main customer interface
│   │   ├── CustomerDashboard.css             # Customer dashboard styling
│   │   ├── Login.jsx                         # User login page
│   │   ├── Login.css                         # Login styling
│   │   ├── Support.jsx                       # Customer support page
│   │   ├── Support.css                       # Support page styling
│   │   └── Login_Temp.jsx                    # Temporary login template
│   │
│   ├── context/
│   │   └── AuthContext.jsx                   # Authentication state management
│   │
│   ├── api/
│   │   └── axiosConfig.js                    # Axios HTTP client setup
│   │
│   ├── assets/
│   │   └── react.svg                         # React logo asset
│   │
│   ├── App.jsx                               # Root application component
│   ├── App.css                               # Global application styling
│   ├── index.css                             # Global styles
│   ├── main.jsx                              # Application entry point
│   │
│   ├── public/
│   │   └── vite.svg                          # Vite logo
│   │
│   ├── package.json                          # Dependencies and scripts
│   ├── vite.config.js                        # Vite configuration
│   ├── eslint.config.js                      # ESLint rules
│   ├── index.html                            # HTML template
│   └── README.md                             # This file
│
└── node_modules/                             # Dependencies (auto-generated)
```

## 🚀 Setup & Installation

### Prerequisites

- **Node.js**: v18 or higher (Download from [nodejs.org](https://nodejs.org/))
- **npm**: v9 or higher (Comes with Node.js)
- **Backend Server**: Running on `http://localhost:9000`

### Step-by-Step Setup

1. **Navigate to Frontend Directory**
   ```bash
   cd frontend/dcms-frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Configure API Connection**
   
   Edit `src/api/axiosConfig.js` and set the backend URL:
   ```javascript
   const API_BASE_URL = 'http://localhost:9000/api';
   
   export const axiosInstance = axios.create({
     baseURL: API_BASE_URL,
     timeout: 10000,
     headers: {
       'Content-Type': 'application/json',
     },
   });
   ```

4. **Verify Backend is Running**
   ```bash
   # Backend should be accessible at
   http://localhost:9000/api/swagger-ui.html
   ```

## 🏃 Running the Application

### Development Mode
```bash
npm run dev
```

The application will start at **http://localhost:5173** with:
- Hot Module Replacement (HMR) for instant updates
- Source maps for debugging
- Development console with React DevTools

### Production Build
```bash
npm run build
```

This creates an optimized production build in the `dist/` directory.

### Preview Production Build
```bash
npm run preview
```

Locally preview the production build before deployment.

### Code Linting
```bash
npm run lint
```

Check code quality and style issues according to ESLint rules.

## 📜 Available Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server with HMR |
| `npm run build` | Create production-optimized build |
| `npm run lint` | Run ESLint to check code quality |
| `npm run preview` | Preview production build locally |

## ✨ Features

### Admin Features
- 📊 **Dashboard**: View system statistics and metrics
- 👥 **Customer Management**: Add, edit, view, and manage customers
- 💰 **Payment Tracking**: Monitor all payments and transactions
- ⚙️ **Rule Engine**: Create and configure dunning rules
- 📋 **Dunning Logs**: View detailed dunning activity logs
- 🎯 **Payment Simulation**: Test dunning scenarios
- 🎫 **Ticket Management**: Handle customer support tickets
- 📈 **Reports**: Generate system reports

### Customer Features
- 👤 **Account Management**: View and update profile
- 💳 **Payment History**: Track all payments and invoices
- 📧 **Notifications**: Receive and view notifications
- 🤖 **AI Support**: Chat with Gemini-powered chatbot
- 📞 **Support Tickets**: Create and track support requests

## 🧩 Components Guide

### Pages

#### Login.jsx
- User authentication page
- Email and password input
- Error handling and validation
- Redirects to dashboard on success

#### AdminDashboard.jsx
- Main admin interface
- Navigation menu with links to admin features
- Summary cards with key metrics
- Quick access to all admin functions

#### CustomerDashboard.jsx
- Main customer interface
- Account information display
- Recent payments and transactions
- Notification center
- Quick access to customer features

#### Support.jsx
- Customer support page
- Chat widget for AI assistance
- Support ticket listing and creation
- FAQ and help resources

### Components

#### ProtectedRoute.jsx
Wrapper component for route protection:
```jsx
<ProtectedRoute role="admin">
  <AdminDashboard />
</ProtectedRoute>
```

#### Admin Components

**CustomerList.jsx**
- Display all customers in table format
- Search and filter functionality
- Edit and delete operations
- Pagination support

**RegisterCustomer.jsx**
- Form to register new customers
- Form validation
- Success/error notifications

**RuleEngine.jsx**
- Create and manage dunning rules
- Configure rule conditions
- Set actions and notifications
- Rule priority management

**DunningLogs.jsx**
- View all dunning activities
- Filter by status, date, customer
- Export log data

**SimulatePayment.jsx**
- Test dunning rule scenarios
- Simulate customer payments
- View predicted outcomes

**TicketManager.jsx**
- View and manage support tickets
- Ticket status updates
- Priority assignment
- Assignment to support team

#### Common Components

**ChatWidget.jsx**
- AI-powered chatbot interface
- Real-time message exchange with Gemini API
- Conversation history
- Minimize/maximize functionality

### Context & State Management

**AuthContext.jsx**
Manages application-wide authentication state:
- User login/logout
- JWT token storage
- User role (admin/customer)
- Protected route access

```jsx
const { user, isAuthenticated, login, logout } = useContext(AuthContext);
```

## 🔌 API Integration

### Axios Configuration

File: `src/api/axiosConfig.js`

```javascript
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const API_BASE_URL = 'http://localhost:9000/api';

export const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add JWT token to all requests
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwtToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle token expiration
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized - refresh token or logout
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
```

### Common API Calls

```javascript
// Authentication
POST /auth/login              // Login user
POST /auth/register           // Register new user
POST /auth/refresh            // Refresh JWT token

// Customers
GET /customers                // List all customers
POST /customers               // Create customer
GET /customers/{id}           // Get customer details
PUT /customers/{id}           // Update customer

// Payments
GET /payments                 // List payments
POST /payments                // Record payment
GET /payments/{id}            // Get payment details

// Dunning
GET /dunning/rules            // List dunning rules
POST /dunning/rules           // Create rule
GET /dunning/logs             // View logs
POST /dunning/simulate        // Simulate payment

// Notifications
GET /notifications            // Get notifications
POST /notifications/send      // Send notification

// Chatbot
POST /chatbot/message         // Send message to AI
GET /chatbot/history          // Get chat history
```

## 🔐 Authentication

### Login Flow

1. User enters credentials on Login page
2. Axios sends POST request to `/auth/login`
3. Backend returns JWT token
4. Token stored in localStorage
5. User redirected to dashboard based on role

### JWT Token Management

```javascript
// Store token
localStorage.setItem('jwtToken', token);

// Retrieve token
const token = localStorage.getItem('jwtToken');

// Decode token to get user info
import { jwtDecode } from 'jwt-decode';
const decoded = jwtDecode(token);
console.log(decoded.role); // 'admin' or 'customer'

// Clear token on logout
localStorage.removeItem('jwtToken');
```

### Protected Routes

Protected routes check user authentication and role:

```jsx
<ProtectedRoute role="admin">
  <AdminDashboard />
</ProtectedRoute>
```

If user is not authenticated or lacks required role, they're redirected to login.

## 🎨 Styling

### Global Styles
- `index.css`: Base styles and CSS variables
- `App.css`: Application root component styles

### Page Styles
- `pages/AdminDashboard.css`: Admin dashboard styling
- `pages/CustomerDashboard.css`: Customer dashboard styling
- `pages/Login.css`: Login page styling
- `pages/Support.css`: Support page styling

### Component Styles
- `components/common/ChatWidget.css`: Chatbot styling

### CSS Conventions
- Use CSS classes with meaningful names
- Mobile-first responsive design
- CSS variables for consistent theming
- Flexbox/Grid for layout

### Responsive Design

Breakpoints:
```css
/* Mobile */
@media (max-width: 640px) { }

/* Tablet */
@media (max-width: 1024px) { }

/* Desktop */
@media (min-width: 1025px) { }
```

## 🧪 Development Tips

### React Best Practices

1. **Use Functional Components with Hooks**
   ```jsx
   function MyComponent() {
     const [state, setState] = useState(null);
     return <div>{state}</div>;
   }
   ```

2. **Use Context for Global State**
   ```jsx
   const { user } = useContext(AuthContext);
   ```

3. **Handle Loading and Error States**
   ```jsx
   const [loading, setLoading] = useState(false);
   const [error, setError] = useState(null);
   ```

4. **Memoize Expensive Components**
   ```jsx
   const MemoizedComponent = React.memo(MyComponent);
   ```

### Vite Features

- **Hot Module Replacement**: Instant updates without page reload
- **Fast Build**: Optimized build times
- **Tree Shaking**: Removes unused code
- **Pre-bundled Dependencies**: Faster dev server starts

### Debugging

1. **React DevTools**: Browser extension for React debugging
2. **Network Tab**: Monitor API calls
3. **Console**: Check for errors and logs
4. **Source Maps**: Debug minified production code

## 🛠️ Troubleshooting

### Issue: Cannot Connect to Backend
**Solution**:
- Verify backend is running on `http://localhost:9000`
- Check `src/api/axiosConfig.js` has correct API_BASE_URL
- Check browser console for CORS errors
- Ensure CORS is configured on backend

### Issue: JWT Token Expired
**Solution**:
- Implement token refresh mechanism
- Clear localStorage and re-login
- Check token expiration time in backend config
- Ensure server and client clocks are synchronized

### Issue: Login Not Working
**Solution**:
- Verify credentials are correct
- Check API endpoint in axiosConfig.js
- Check browser console for error messages
- Verify backend is running and accepting requests

### Issue: Page Not Loading
**Solution**:
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Clear Vite cache
rm -rf dist .vite

# Restart dev server
npm run dev
```

### Issue: Styling Not Applied
**Solution**:
- Check CSS class names match in JSX
- Verify CSS file is imported
- Clear browser cache
- Check CSS specificity issues
- Restart dev server

### Issue: Module Not Found
**Solution**:
```bash
# Reinstall dependencies
npm install

# Check import paths are correct
# Verify file exists in correct location
# Check for typos in file names (case-sensitive on Linux/Mac)
```

## 📦 npm Commands Reference

```bash
# Install all dependencies
npm install

# Install specific package
npm install package-name

# Install dev dependency
npm install --save-dev package-name

# Remove dependency
npm uninstall package-name

# Update all dependencies
npm update

# List installed packages
npm list

# Check for security vulnerabilities
npm audit

# Fix vulnerabilities
npm audit fix
```

## 🚀 Deployment

### Build for Production
```bash
npm run build
```

This creates a `dist/` folder with optimized production files.

### Deployment Options

1. **Netlify**
   - Connect GitHub repository
   - Set build command: `npm run build`
   - Set publish directory: `dist`

2. **Vercel**
   - Connect GitHub repository
   - Framework: React
   - Build command: `npm run build`

3. **GitHub Pages**
   - Configure for SPA (single page application)
   - Use environment variable for API URL

4. **Traditional Server**
   - Copy `dist/` contents to web server
   - Configure server to serve `index.html` for all routes
   - Set backend API URL via environment variables

## 📚 Additional Resources

- [React Documentation](https://react.dev)
- [Vite Documentation](https://vitejs.dev)
- [Axios Documentation](https://axios-http.com)
- [React Router Documentation](https://reactrouter.com)
- [JWT Introduction](https://jwt.io)
- [MDN Web Docs](https://developer.mozilla.org)

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/new-feature`
2. Make your changes
3. Run linting: `npm run lint`
4. Commit your changes: `git commit -m "Add new feature"`
5. Push to branch: `git push origin feature/new-feature`
6. Create a Pull Request

## 📝 Code Style

- Use camelCase for variables and functions
- Use PascalCase for React components
- Use meaningful descriptive names
- Keep functions small and focused
- Add comments for complex logic
- Use JSX best practices

## 📞 Support & Issues

For issues, bugs, or feature requests:
1. Check existing issues in the repository
2. Create a detailed issue report with:
   - Steps to reproduce
   - Expected vs actual behavior
   - Browser and OS information
   - Screenshots if applicable
3. Contact the development team

---

**Frontend Version**: 1.0.0  
**Last Updated**: January 2026  
**Status**: Active Development

## 🎯 Quick Start Checklist

- [ ] Node.js v18+ installed
- [ ] Ran `npm install` to install dependencies
- [ ] Backend running on `http://localhost:9000`
- [ ] Updated `src/api/axiosConfig.js` with correct backend URL
- [ ] Ran `npm run dev` to start development server
- [ ] Frontend accessible at `http://localhost:5173`
- [ ] Can log in with test credentials
- [ ] Admin/Customer dashboard loading correctly
- [ ] API calls working in browser console
- [ ] ESLint passing with `npm run lint`
