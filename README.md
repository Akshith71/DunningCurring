# Dunning & Currying Management System (DCMS)

A comprehensive platform for managing dunning processes and customer payment recovery through intelligent automation and real-time notifications.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## 📌 Project Overview

**DCMS** is a full-stack web application designed to:

- **Automate Dunning Processes**: Intelligently manage payment recovery for failed transactions
- **Define Custom Rules**: Create and manage flexible dunning rules based on business requirements
- **Track Payment Status**: Monitor customer payments and transaction history in real-time
- **Generate Notifications**: Automated email and in-app notifications for payment reminders
- **AI-Powered Chat**: Gemini-integrated chatbot for customer support
- **Telecom Service Management**: Manage telecom service billing and recovery
- **Admin Dashboard**: Comprehensive admin interface for system management
- **Customer Portal**: Customer-facing interface to manage their accounts and payments

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (React + Vite)              │
│         Admin Dashboard | Customer Dashboard             │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP/REST API
┌────────────────────▼────────────────────────────────────┐
│                Backend (Spring Boot 4.0.1)              │
│  Controllers | Services | Repositories | Entities       │
│  Security (JWT) | Schedulers | Batch Jobs               │
└────────────────────┬────────────────────────────────────┘
                     │ SQL
┌────────────────────▼────────────────────────────────────┐
│            Database (MySQL)                             │
│            dcsm_db                                       │
└─────────────────────────────────────────────────────────┘
```

## 💻 Tech Stack

### Backend
- **Framework**: Spring Boot 4.0.1
- **Language**: Java 17
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security + JWT (JSON Web Tokens)
- **API Documentation**: Swagger/OpenAPI
- **Email Service**: Spring Mail (Gmail SMTP)
- **External APIs**: Gemini AI, OkHttp
- **Build Tool**: Maven

### Frontend
- **Framework**: React 19.2.0
- **Build Tool**: Vite 7.2.4
- **HTTP Client**: Axios
- **Routing**: React Router DOM 7.11.0
- **Authentication**: JWT Decode
- **Linting**: ESLint

### External Services
- **Mail Service**: Gmail SMTP
- **AI Service**: Google Gemini API
- **Package Management**: npm

## 📦 Prerequisites

### System Requirements
- **Java**: JDK 17 or higher
- **Node.js**: v18 or higher
- **npm**: v9 or higher
- **MySQL**: v8.0 or higher
- **Git**: Latest version

### Required Accounts/Keys
- **Gmail Account**: For email notifications (App-specific password required)
- **Google Gemini API Key**: For AI chatbot functionality
- **MySQL Database**: Local or remote MySQL instance

## 📁 Project Structure

```
DCMS/
├── DCMS/                           # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/DCMS/
│   │   │   │   ├── DcmsApplication.java          # Main Application
│   │   │   │   ├── batch/                        # Batch Jobs
│   │   │   │   ├── config/                       # Configuration
│   │   │   │   ├── Constants/                    # Application Constants
│   │   │   │   ├── Controller/                   # REST Endpoints
│   │   │   │   ├── DTO/                          # Data Transfer Objects
│   │   │   │   ├── Entity/                       # JPA Entities
│   │   │   │   ├── Exception/                    # Custom Exceptions
│   │   │   │   ├── Repository/                   # Data Access Layer
│   │   │   │   ├── Scheduler/                    # Task Schedulers
│   │   │   │   ├── Security/                     # JWT & Security
│   │   │   │   └── Servicelayer/                 # Business Logic
│   │   │   └── resources/
│   │   │       └── application.properties        # Configuration
│   │   └── test/                                 # Unit Tests
│   ├── pom.xml                                   # Maven Configuration
│   ├── mvnw & mvnw.cmd                           # Maven Wrapper
│   └── HELP.md                                   # Spring Boot Help
│
├── frontend/dcms-frontend/                       # Frontend (React)
│   ├── src/
│   │   ├── components/
│   │   │   ├── admin/                            # Admin Components
│   │   │   ├── common/                           # Shared Components
│   │   │   └── ProtectedRoute.jsx                # Route Protection
│   │   ├── pages/
│   │   │   ├── AdminDashboard.jsx                # Admin Interface
│   │   │   ├── CustomerDashboard.jsx             # Customer Interface
│   │   │   ├── Login.jsx                         # Login Page
│   │   │   └── Support.jsx                       # Support Page
│   │   ├── context/
│   │   │   └── AuthContext.jsx                   # Auth State Management
│   │   ├── api/
│   │   │   └── axiosConfig.js                    # Axios Configuration
│   │   ├── App.jsx                               # Root Component
│   │   └── main.jsx                              # Entry Point
│   ├── public/                                   # Static Assets
│   ├── package.json                              # Dependencies
│   ├── vite.config.js                            # Vite Configuration
│   ├── eslint.config.js                          # ESLint Configuration
│   ├── index.html                                # HTML Template
│   └── README.md                                 # Frontend Documentation
│
├── README.md                                     # This File
├── .gitignore                                    # Git Configuration
└── .metadata/                                    # IDE Metadata
```

## 🚀 Setup Instructions

### Backend Setup

1. **Clone the repository**
   ```bash
   cd DCMS/DCMS
   ```

2. **Configure Database**
   - Create MySQL database: `dcsm_db`
   - Update `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/dcsm_db
     spring.datasource.username=your_mysql_user
     spring.datasource.password=your_mysql_password
     ```

3. **Configure Email Service**
   - Update Gmail credentials in `application.properties`:
     ```properties
     spring.mail.username=your_gmail@gmail.com
     spring.mail.password=your_app_specific_password
     ```
   - Generate Gmail App-specific password from your Google Account settings

4. **Configure JWT**
   - JWT secret and expiration are already configured in `application.properties`
   - Modify if needed for your security requirements

5. **Install Dependencies**
   ```bash
   mvn clean install
   ```

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend/dcms-frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Configure API Connection**
   - Update `src/api/axiosConfig.js` with backend URL (default: `http://localhost:9000`)

## 🏃 Running the Application

### Backend

```bash
cd DCMS/DCMS

# Using Maven
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/DCMS-0.0.1-SNAPSHOT.jar
```

Backend runs on: **http://localhost:9000**

**API Documentation (Swagger)**: http://localhost:9000/swagger-ui.html

### Frontend

```bash
cd frontend/dcms-frontend

# Development Mode
npm run dev

# Production Build
npm run build

# Preview Production Build
npm run preview
```

Frontend runs on: **http://localhost:5173** (Vite default)

### Running Both Simultaneously

Open two terminals:

**Terminal 1 - Backend**
```bash
cd DCMS/DCMS
mvn spring-boot:run
```

**Terminal 2 - Frontend**
```bash
cd frontend/dcms-frontend
npm run dev
```

## 📚 API Documentation

### Core Endpoints

- **Authentication**
  - `POST /api/auth/register` - User registration
  - `POST /api/auth/login` - User login
  - `POST /api/auth/refresh` - Refresh JWT token

- **Customer Management**
  - `GET /api/customers` - List all customers
  - `POST /api/customers` - Create new customer
  - `GET /api/customers/{id}` - Get customer details
  - `PUT /api/customers/{id}` - Update customer

- **Payments**
  - `GET /api/payments` - List payments
  - `POST /api/payments` - Record payment
  - `GET /api/payments/{id}` - Get payment details

- **Dunning**
  - `GET /api/dunning/rules` - List dunning rules
  - `POST /api/dunning/rules` - Create rule
  - `GET /api/dunning/logs` - View dunning logs
  - `POST /api/dunning/simulate` - Simulate payment

- **Notifications**
  - `GET /api/notifications` - Get notifications
  - `POST /api/notifications/send` - Send notification

- **Admin**
  - `GET /api/admin/dashboard` - Dashboard metrics
  - `GET /api/admin/services` - Telecom services

**Full API Documentation**: Access Swagger UI at `/swagger-ui.html` when backend is running

## 🔐 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Spring Security**: Integrated security framework
- **Role-Based Access Control**: Admin and Customer roles
- **Protected Routes**: Frontend route protection based on authentication
- **CORS Configuration**: Cross-origin resource sharing configured
- **Password Encryption**: Secure password storage

## 📧 Key Features

- ✅ **Automated Dunning**: Scheduled payment recovery processes
- ✅ **Custom Rules**: Business logic-driven dunning rules
- ✅ **Real-Time Notifications**: Email and in-app notifications
- ✅ **AI Chatbot**: Gemini-powered customer support
- ✅ **Payment Tracking**: Comprehensive payment history
- ✅ **Admin Dashboard**: System monitoring and management
- ✅ **Customer Portal**: Self-service customer interface
- ✅ **Batch Processing**: Scheduled batch jobs for data processing

## 📝 Configuration Files

### Backend Configuration
- `DCMS/DCMS/src/main/resources/application.properties` - Spring Boot configuration
- `DCMS/DCMS/pom.xml` - Maven dependencies and build config

### Frontend Configuration
- `frontend/dcms-frontend/package.json` - npm dependencies
- `frontend/dcms-frontend/vite.config.js` - Vite bundler config
- `frontend/dcms-frontend/eslint.config.js` - Code linting rules

## 🧪 Testing

### Backend Tests
```bash
cd DCMS/DCMS
mvn test
```

### Frontend Linting
```bash
cd frontend/dcms-frontend
npm run lint
```

## 📖 Documentation

For detailed information, see:
- [Backend Documentation](DCMS/README.md)
- [Frontend Documentation](frontend/dcms-frontend/README.md)

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit your changes: `git commit -m 'Add your feature'`
3. Push to the branch: `git push origin feature/your-feature`
4. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For issues, questions, or contributions, please contact the development team or open an issue in the repository.

---

**Last Updated**: January 2026
**Version**: 1.0.0
**Status**: Active Development
