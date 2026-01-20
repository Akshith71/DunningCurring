# DCMS Backend API

Spring Boot REST API for Dunning & Currying Management System. This backend service handles all business logic for dunning processes, customer management, payments, and notifications.

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Security](#security)
- [Scheduling & Batch Jobs](#scheduling--batch-jobs)
- [Troubleshooting](#troubleshooting)

## 📌 Overview

The DCMS Backend is a robust REST API built with Spring Boot 4.0.1 that manages:

- **Authentication & Authorization**: JWT-based user authentication and role-based access control
- **Customer Management**: CRUD operations for customer data and profiles
- **Payment Processing**: Payment recording, tracking, and reconciliation
- **Dunning Engine**: Automated dunning rule execution and payment recovery
- **Notifications**: Email notifications and in-app notification management
- **Telecom Services**: Management of telecom billing and services
- **Admin Functions**: System administration and monitoring
- **AI Integration**: Gemini-powered chatbot for customer support
- **Scheduled Tasks**: Automated batch jobs and scheduled processes

## 💻 Tech Stack

- **Java 17**: Latest LTS Java version
- **Spring Boot 4.0.1**: Modern web framework
- **Spring Data JPA**: ORM and database abstraction
- **Spring Security**: Authentication and authorization
- **MySQL 8.0+**: Relational database
- **JWT (JJWT 0.11.5)**: Token-based authentication
- **Swagger/OpenAPI 2.5.0**: API documentation
- **Lombok**: Boilerplate code reduction
- **Gmail SMTP**: Email delivery service
- **OkHttp 4.10.0**: HTTP client
- **Gemini API**: AI chatbot integration
- **Maven**: Build and dependency management

## 📁 Project Structure

```
DCMS/
├── src/main/java/com/example/DCMS/
│   ├── DcmsApplication.java                # Main Spring Boot application
│   │
│   ├── batch/
│   │   └── DailyBatchJob.java             # Scheduled batch jobs
│   │
│   ├── config/
│   │   ├── CorsConfig.java                # CORS configuration
│   │   ├── SwaggerConfig.java             # Swagger/API documentation
│   │   └── /* Other configurations */
│   │
│   ├── Constants/
│   │   └── /* Application constants */    # Static constant values
│   │
│   ├── Controller/
│   │   ├── AdminController.java           # Admin endpoints
│   │   ├── AdminCustomerController.java   # Admin customer management
│   │   ├── AdminServiceController.java    # Admin service management
│   │   ├── AdminTelecomServiceController.java  # Admin telecom services
│   │   ├── AuthController.java            # Authentication endpoints
│   │   ├── ChatBotController.java         # AI chatbot endpoints
│   │   ├── CustomerController.java        # Customer endpoints
│   │   ├── DunningController.java         # Dunning process endpoints
│   │   ├── DunningRuleController.java     # Dunning rule management
│   │   ├── NotificationController.java    # Notification endpoints
│   │   └── PaymentController.java         # Payment endpoints
│   │
│   ├── DTO/
│   │   └── /* Data Transfer Objects */    # API request/response models
│   │
│   ├── Entity/
│   │   ├── Customer.java                  # Customer entity
│   │   ├── Payment.java                   # Payment entity
│   │   ├── DunningRule.java               # Dunning rule entity
│   │   ├── DunningLog.java                # Dunning log entity
│   │   ├── CuringAction.java              # Curing action entity
│   │   ├── Notification.java              # Notification entity
│   │   ├── TelecomService.java            # Telecom service entity
│   │   ├── Ticket.java                    # Support ticket entity
│   │   └── User.java                      # User entity
│   │
│   ├── Exception/
│   │   └── /* Custom exceptions */        # Application-specific exceptions
│   │
│   ├── Repository/
│   │   ├── CustomerRepository.java        # Customer data access
│   │   ├── PaymentRepository.java         # Payment data access
│   │   ├── DunningRuleRepository.java     # Dunning rule data access
│   │   ├── DunningLogRepository.java      # Dunning log data access
│   │   ├── NotificationRepository.java    # Notification data access
│   │   └── /* Other repositories */       # JPA Spring Data repositories
│   │
│   ├── Scheduler/
│   │   └── DunningScheduler.java          # Dunning process scheduler
│   │
│   ├── Security/
│   │   ├── JwtAuthenticationFilter.java   # JWT request filter
│   │   ├── JwtTokenProvider.java          # JWT token generation/validation
│   │   └── SecurityConfig.java            # Spring Security configuration
│   │
│   └── Servicelayer/
│       ├── CustomerService.java           # Customer business logic
│       ├── PaymentService.java            # Payment business logic
│       ├── DunningEngineService.java      # Core dunning logic
│       ├── DunningRuleService.java        # Dunning rule management
│       ├── CuringActionService.java       # Curing action management
│       ├── NotificationService.java       # Notification management
│       ├── EmailService.java              # Email sending logic
│       ├── GeminiService.java             # Gemini AI integration
│       ├── TelecomServiceService.java     # Telecom service management
│       └── UserService.java               # User business logic
│
├── src/main/resources/
│   └── application.properties              # Application configuration
│
├── src/test/java/
│   └── /* Unit and integration tests */
│
├── pom.xml                                 # Maven configuration
├── mvnw & mvnw.cmd                         # Maven wrapper
└── HELP.md                                 # Spring Boot help
```

## 🚀 Setup & Installation

### Prerequisites

- **Java 17+**: Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/#java17)
- **MySQL 8.0+**: Download from [mysql.com](https://www.mysql.com/downloads/)
- **Maven 3.6+**: Download from [maven.apache.org](https://maven.apache.org/download.cgi)
- **Git**: For version control

### Step-by-Step Setup

1. **Clone and Navigate**
   ```bash
   cd DCMS/DCMS
   ```

2. **Create MySQL Database**
   ```sql
   CREATE DATABASE dcsm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Update Configuration**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/dcsm_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   
   # JPA/Hibernate Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=false
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   
   # JWT Configuration
   jwt.secret=your_secret_key_min_32_characters_long
   jwt.expiration=86400000  # 24 hours in milliseconds
   
   # Email Configuration (Gmail)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_specific_password  # Not your actual Gmail password!
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   spring.mail.properties.mail.smtp.starttls.required=true
   
   # Server Configuration
   server.port=9000
   server.servlet.context-path=/api
   
   # Application Name
   spring.application.name=DCMS
   ```

   **Note**: For Gmail, generate an [App-specific password](https://myaccount.google.com/apppasswords) instead of using your actual password.

4. **Install Dependencies**
   ```bash
   mvn clean install
   ```

5. **Build the Project**
   ```bash
   mvn clean package
   ```

## 🏃 Running the Application

### Option 1: Direct Maven Run
```bash
mvn spring-boot:run
```

### Option 2: Build and Run JAR
```bash
mvn clean package
java -jar target/DCMS-0.0.1-SNAPSHOT.jar
```

### Option 3: Using Maven Wrapper (No Maven Installation Required)
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Verify Backend is Running
- Application URL: `http://localhost:9000/api`
- Swagger UI: `http://localhost:9000/api/swagger-ui.html`
- API Docs: `http://localhost:9000/api/v3/api-docs`

## 📚 API Endpoints

### Authentication Endpoints
```
POST   /api/auth/register              Register new user
POST   /api/auth/login                 Login user and get JWT token
POST   /api/auth/refresh               Refresh JWT token
GET    /api/auth/profile               Get current user profile
```

### Customer Management
```
GET    /api/customers                  List all customers
GET    /api/customers/{id}             Get customer by ID
POST   /api/customers                  Create new customer
PUT    /api/customers/{id}             Update customer
DELETE /api/customers/{id}             Delete customer
GET    /api/customers/search            Search customers
```

### Payment Management
```
GET    /api/payments                   List all payments
GET    /api/payments/{id}              Get payment by ID
POST   /api/payments                   Record new payment
PUT    /api/payments/{id}              Update payment
GET    /api/payments/customer/{customerId}  Get customer payments
GET    /api/payments/status/{status}   Get payments by status
```

### Dunning Management
```
GET    /api/dunning/rules              List all dunning rules
GET    /api/dunning/rules/{id}         Get rule by ID
POST   /api/dunning/rules              Create dunning rule
PUT    /api/dunning/rules/{id}         Update dunning rule
DELETE /api/dunning/rules/{id}         Delete dunning rule
GET    /api/dunning/logs               Get dunning logs
POST   /api/dunning/simulate           Simulate payment
GET    /api/dunning/status             Get dunning status
```

### Notification Management
```
GET    /api/notifications              Get all notifications
GET    /api/notifications/{id}         Get notification by ID
POST   /api/notifications              Create notification
PUT    /api/notifications/{id}         Update notification
POST   /api/notifications/send         Send notification
GET    /api/notifications/customer/{customerId}  Customer notifications
```

### Telecom Services
```
GET    /api/services                   List all services
GET    /api/services/{id}              Get service by ID
POST   /api/services                   Create service
PUT    /api/services/{id}              Update service
DELETE /api/services/{id}              Delete service
```

### Admin Endpoints
```
GET    /api/admin/dashboard            Dashboard statistics
GET    /api/admin/dashboard/summary    Summary metrics
POST   /api/admin/customers            Admin customer management
GET    /api/admin/services             Admin service management
POST   /api/admin/telecom-services     Admin telecom services
GET    /api/admin/reports              Generate reports
```

### Chatbot Endpoints
```
POST   /api/chatbot/message            Send message to AI chatbot
GET    /api/chatbot/history            Get conversation history
POST   /api/chatbot/clear              Clear conversation history
```

**Full API documentation available at**: `http://localhost:9000/api/swagger-ui.html`

## 🗄️ Database Schema

### Core Entities

- **User**: System users with roles (Admin, Customer)
- **Customer**: Customer profile and account information
- **Payment**: Payment records and transaction history
- **DunningRule**: Rules for automated dunning processes
- **DunningLog**: Log of dunning actions performed
- **CuringAction**: Actions taken to recover payments
- **Notification**: Notification history and status
- **TelecomService**: Telecom service offerings
- **Ticket**: Customer support tickets

### Key Relationships

```
User ←→ Customer
Customer ←→ Payment (1:M)
Customer ←→ DunningLog (1:M)
DunningRule ←→ DunningLog (1:M)
Customer ←→ Notification (1:M)
Customer ←→ CuringAction (1:M)
Customer ←→ Ticket (1:M)
```

## ⚙️ Configuration

### application.properties

Key configuration sections:

```properties
# Spring Application
spring.application.name=DCMS

# Database
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/dcsm_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=MySuperSecretKeyForDCMSApplicationEnsureItIsLongEnough123
jwt.expiration=86400000

# Email/Mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=ponugotiakshit@gmail.com
spring.mail.password=gqzm ltzy xrby fvdb
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Server
server.port=9000
```

### Spring Security Configuration

- JWT-based authentication
- CORS enabled for frontend communication
- Role-based access control (RBAC)
- Protected endpoints require valid JWT token

### CORS Configuration

CORS is configured to allow requests from frontend (http://localhost:5173) for development.

## 🔐 Security

### Authentication Flow

1. User registers/logs in via `/api/auth/login`
2. Server validates credentials and generates JWT token
3. Token is returned to frontend and stored
4. Frontend includes token in `Authorization: Bearer {token}` header
5. Backend validates token on each request
6. Token expires after configured time (default: 24 hours)

### Security Headers

- Secure password storage with encryption
- JWT expiration and refresh tokens
- CORS configured for trusted origins
- Spring Security filters for request validation

### Best Practices

- Keep JWT secret secure and long (minimum 32 characters)
- Use HTTPS in production
- Implement rate limiting for authentication endpoints
- Regularly rotate JWT secrets
- Store sensitive data (passwords, emails) encrypted

## ⏰ Scheduling & Batch Jobs

### DunningScheduler

Automatically executes dunning processes at configured intervals:
- Checks for overdue payments
- Applies dunning rules
- Generates notifications
- Creates dunning logs

### DailyBatchJob

Runs daily batch processes:
- Data reconciliation
- Report generation
- Cleanup operations
- Scheduled maintenance tasks

Enable scheduling in `DcmsApplication.java`:
```java
@EnableScheduling
@SpringBootApplication
public class DcmsApplication { }
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=CustomerServiceTest
```

### Test Coverage
```bash
mvn jacoco:report
```

## 🛠️ Troubleshooting

### Issue: Cannot Connect to MySQL
**Solution**: 
- Verify MySQL is running: `mysql -u root -p`
- Check connection string in `application.properties`
- Ensure database `dcsm_db` exists

### Issue: JWT Token Expired
**Solution**:
- Use refresh endpoint to get new token
- Increase `jwt.expiration` in configuration
- Ensure server and client clocks are synchronized

### Issue: Email Not Sending
**Solution**:
- Verify Gmail credentials in `application.properties`
- Use App-specific password (not regular Gmail password)
- Enable "Less secure app access" in Gmail settings
- Check internet connection

### Issue: CORS Error
**Solution**:
- Verify frontend URL is in CORS whitelist
- Check `CorsConfig.java` configuration
- Ensure credentials are sent with requests

### Issue: Port 9000 Already in Use
**Solution**:
```bash
# Change port in application.properties
server.port=9001

# Or kill existing process on port 9000
# Windows: netstat -ano | findstr :9000
# Linux: lsof -i :9000 | kill -9 <PID>
```

## 📦 Maven Commands

```bash
# Clean and build
mvn clean package

# Install dependencies
mvn install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Generate documentation
mvn javadoc:javadoc

# Check code quality
mvn checkstyle:check
```

## 📖 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Swagger/OpenAPI Documentation](https://springdoc.org/)
- [JWT Introduction](https://jwt.io/)

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/new-feature`
2. Make your changes and commit: `git commit -m "Add new feature"`
3. Push to branch: `git push origin feature/new-feature`
4. Create a Pull Request

## 📝 Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add Javadoc comments for public methods
- Keep methods small and focused
- Use Lombok annotations to reduce boilerplate

## 📞 Support & Issues

For issues, bugs, or feature requests:
1. Check existing issues in the repository
2. Create a detailed issue report
3. Include error logs and steps to reproduce
4. Contact the development team

---

**Backend Version**: 1.0.0  
**Last Updated**: January 2026  
**Status**: Active Development
