# BookMyShow (BMS) - System Design Learning Project

## 🎯 Project Goals

This project is designed to learn and practice:
- **System Design** principles and patterns
- **Design Patterns** (Creational, Structural, Behavioral)
- **Software Architecture** (Layered, Clean, Hexagonal, etc.)
- **Low-Level Design (LLD)** concepts
- **When to use which pattern/architecture** - framework agnostic understanding

The focus is on **learning** rather than building a production-ready system. We'll implement features in phases to manage complexity and learn effectively.

---

# 🚀 QUICK START (Setup & Run)

> **Just want to run the project?** Follow this section. Skip to "📚 Learning & Knowledge" section for concepts.

## Prerequisites

Install these on your system (any OS - Windows/Mac/Linux):

1. **Java 17+** - [Download](https://adoptium.net/)
2. **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
3. **Docker & Docker Compose** - [Download](https://www.docker.com/get-started)
4. **SendGrid Account** (Free) - [Sign Up](https://sendgrid.com) (for email functionality)

---

## Step 1: Clone & Navigate

```bash
git clone <repository-url>
cd BMS
```

---

## Step 2: Configure SendGrid (Optional for testing)

### Option A: Use Environment Variables
```bash
# Windows (PowerShell)
$env:SENDGRID_API_KEY="your-api-key"
$env:SENDGRID_FROM_EMAIL="your-email@example.com"

# Mac/Linux
export SENDGRID_API_KEY="your-api-key"
export SENDGRID_FROM_EMAIL="your-email@example.com"
```

### Option B: Edit `application.yml`
Update these lines in `src/main/resources/application.yml`:
```yaml
email:
  sendgrid:
    api-key: your-api-key-here
    from-email: your-verified-email@example.com
```

**Note:** For testing without SendGrid, emails will be logged to console.

---

## Step 3: Run the Project

### Method 1: Docker Compose (Recommended - Easiest)

```bash
# Start everything (PostgreSQL + Java app)
docker-compose up -d --build

# Check if running
docker-compose ps

# View logs
docker-compose logs -f app
```

**Access:**
- Application: http://localhost:8080
- PostgreSQL: localhost:5432

**Stop:**
```bash
docker-compose down
```

### Method 2: Local Development (Faster for coding)

```bash
# Start only PostgreSQL
docker-compose up -d postgres

# Run Java app locally (auto-reload on code changes)
mvn spring-boot:run
```

**Access:** http://localhost:8080

---

## Step 4: Test the API

### Quick Test - Complete Flow:

```bash
# 1. Register
curl -X POST http://localhost:8080/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# 2. Check console/email for verification code, then verify
curl -X POST http://localhost:8080/verify-email \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","code":"123456"}'

# 3. Login (get JWT token)
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# 4. Use token for protected endpoints
curl -X DELETE http://localhost:8080/delete-account \
  -H "Authorization: Bearer <your-token-from-step-3>"
```

---

## Common Commands

### Docker Commands:
```bash
# Start services
docker-compose up -d

# Stop services
docker-compose stop

# View logs
docker-compose logs -f app

# Rebuild after code changes
docker-compose up -d --build app

# Remove everything (including data)
docker-compose down -v
```

### Maven Commands:
```bash
# Build project
mvn clean package

# Run locally
mvn spring-boot:run

# Run tests
mvn test
```

---

## Troubleshooting

### Port Already in Use
```bash
# Check what's using port 8080
# Windows: netstat -ano | findstr :8080
# Mac/Linux: lsof -i :8080

# Change port in application.yml if needed
server:
  port: 8081
```

### Database Connection Error
```bash
# Check if PostgreSQL is running
docker-compose ps

# Check PostgreSQL logs
docker-compose logs postgres

# Restart PostgreSQL
docker-compose restart postgres
```

### Application Won't Start
```bash
# Check logs
docker-compose logs app

# Rebuild
docker-compose up -d --build app
```

---

# 📡 API DOCUMENTATION

> **📄 OpenAPI Specification:** For frontend developers, see `openapi.yaml` for complete API documentation with request/response schemas, examples, and interactive API explorer support.

## Base URL
```
http://localhost:8080
```

## Authentication Endpoints

### 1. Register User
```bash
POST /signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response (201):**
```json
{
  "respText": "Account created successfully. Please check your email for verification code."
}
```

### 2. Verify Email
```bash
POST /verify-email
Content-Type: application/json

{
  "email": "user@example.com",
  "code": "123456"
}
```
**Response (200):**
```json
{
  "respText": "Email verified successfully. Your account is now active!"
}
```

### 3. Login
```bash
POST /login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiryEpoch": 1234567890000
}
```

### 4. Forgot Password
```bash
POST /forgot-password
Content-Type: application/json

{
  "email": "user@example.com"
}
```

### 5. Verify Reset Code
```bash
POST /verify-reset-code
Content-Type: application/json

{
  "email": "user@example.com",
  "code": "123456"
}
```

### 6. Reset Password
```bash
POST /reset-password
Content-Type: application/json

{
  "email": "user@example.com",
  "code": "123456",
  "newPassword": "newpassword123"
}
```

### 7. Delete Account (Protected)
```bash
DELETE /delete-account
Authorization: Bearer <your-jwt-token>
```

---

## 🔐 JWT Token Usage

### Getting a Token:
1. Register → Verify email → Login
2. Token is returned in login response

### Using the Token:
```bash
# Include in Authorization header
Authorization: Bearer <your-jwt-token>
```

### Token Details:
- **Algorithm:** HS256
- **Expiry:** 1 hour
- **Claims:** `sub`, `email`, `userId`, `iat`, `exp`

---

## ⚠️ Error Responses

All errors follow this format:
```json
{
  "errorCode": "UNAUTHORIZED",
  "message": "Invalid email or password",
  "statusCode": 401,
  "timestamp": "2024-01-15T10:30:00",
  "path": "/login"
}
```

**Error Codes:**
- `RESOURCE_NOT_FOUND` (404)
- `VALIDATION_ERROR` (400)
- `UNAUTHORIZED` (401)
- `FORBIDDEN` (403)
- `CONFLICT` (409)

---

# 📚 LEARNING & KNOWLEDGE

> **Want to understand the concepts?** Read this section.

---

## 📋 Product Requirements

### **Phase 1: Core Booking System (MVP)**

**Goal:** Build a basic movie ticket booking system with essential features.

#### Functional Requirements:

1. **User Management** ✅
   - ✅ User registration with email verification
   - ✅ User login with JWT authentication
   - ✅ Forgot password with email verification
   - ✅ Delete account (JWT protected)
   - ⏳ User profile management (TODO)

2. **Theater & Movie Management**
   - Add/view theaters
   - Add/view movies
   - Theater has multiple screens
   - Each screen has seats (rows and columns)
   - Movie can be shown in multiple theaters

3. **Show Management**
   - Create shows for a movie in a theater screen
   - Show has: movie, theater, screen, date, time, price
   - View available shows

4. **Booking System**
   - View available seats for a show
   - Select seats and book tickets
   - Calculate total price
   - Basic booking confirmation

5. **Payment**
   - Simple payment processing (mock/simulated)
   - Payment confirmation

---

### **Phase 2: Enhanced Features & Scalability**

**Goal:** Add advanced features and handle concurrent bookings.

#### Functional Requirements:

1. **Enhanced Booking**
   - Seat locking mechanism (temporary hold)
   - Booking timeout (auto-release after X minutes)
   - Multiple seat selection
   - Booking history for users

2. **Search & Filter**
   - Search movies by name, genre, language
   - Filter shows by date, time, theater
   - Sort by price, rating, time

3. **User Features**
   - Booking cancellation (with refund policy)
   - User ratings and reviews for movies
   - Favorite theaters/movies

4. **Admin Features**
   - Admin panel for theater/movie management
   - View booking statistics
   - Manage show schedules

5. **Notifications**
   - Booking confirmation notifications
   - Reminder notifications before show

---

### **Phase 3: Advanced Features & Optimization**

**Goal:** Add complex features and optimize for performance.

#### Functional Requirements:

1. **Advanced Booking**
   - Waitlist for sold-out shows
   - Group booking discounts
   - Loyalty points and rewards
   - Gift vouchers

2. **Recommendations**
   - Movie recommendations based on user history
   - Similar movie suggestions
   - Popular shows in user's area

3. **Analytics & Reporting**
   - Revenue reports
   - Popular movies/theaters analytics
   - User behavior analytics
   - Peak time analysis

4. **Multi-tenancy**
   - Support for multiple cities
   - City-based theater and show management
   - Location-based recommendations

5. **Integration Features**
   - Email service integration
   - SMS notifications
   - Payment gateway integration (mock)
   - QR code generation for tickets

---

## 🛠️ Technology Stack

### Backend:
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **Hibernate** - ORM implementation
- **PostgreSQL** - Relational database
- **Maven** - Build tool

### Security & Authentication:
- **JWT (JSON Web Tokens)** - Token-based authentication
- **jjwt** - JWT library
- **SHA-256** - Password hashing

### Email Service:
- **SendGrid** - Email delivery service
- **HTML Email Templates** - Beautiful email designs

### Infrastructure:
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

### Libraries:
- **Lombok** - Boilerplate reduction
- **Jakarta Validation** - Input validation
- **Jackson** - JSON processing
- **Commons Codec** - Password hashing utilities

---

## 🏗️ Architecture

### Current Structure:
```
com
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entities/DTOs
├── util/           # Utility classes
└── exception/      # Custom exceptions
```

### Design Principles:
1. **SOLID Principles**
2. **DRY (Don't Repeat Yourself)**
3. **Separation of Concerns**
4. **Dependency Inversion**
5. **Single Responsibility**

---

## ✅ Implemented Features

### User Management:
- ✅ User registration with email verification (SendGrid integration)
- ✅ JWT-based authentication
- ✅ Password reset flow with email verification
- ✅ Account deletion (JWT protected)
- ✅ Beautiful HTML email templates

### Error Handling:
- ✅ Custom exception hierarchy
- ✅ Global exception handler (`@RestControllerAdvice`)
- ✅ Standardized error responses
- ✅ Exception-driven error handling pattern

### Infrastructure:
- ✅ Docker Compose setup (PostgreSQL + Java app)
- ✅ Environment variable configuration
- ✅ Spring Data JPA integration
- ✅ Email service integration (SendGrid)

---

## 🎓 Learning Outcomes

By the end of this project, you should understand:

1. **When to use which design pattern:**
   - Factory vs Builder vs Prototype
   - Strategy vs Template Method
   - Observer vs Pub-Sub
   - Singleton vs Static classes

2. **Architecture decisions:**
   - When to use layered vs hexagonal architecture
   - When to introduce service layer
   - When to use repository pattern
   - When to add caching layer

3. **System design concepts:**
   - Database design and normalization
   - Concurrency handling
   - Scalability considerations
   - Performance optimization

4. **Framework-agnostic understanding:**
   - Concepts that apply to any framework
   - Patterns that work across languages
   - Architecture principles independent of technology

---

## 📝 Development Notes

- **Focus on learning**, not perfection
- **Ask for suggestions** when stuck on design decisions
- **Implement incrementally** - one feature at a time
- **Refactor as you learn** - it's okay to improve previous code
- **Document your decisions** - why you chose a particular pattern/approach

---

## 📚 Additional Documentation

- **Docker Setup:** See `DOCKER_SETUP.md` for detailed Docker documentation
- **Error Handling:** See `ERROR_HANDLING_PATTERNS.md` for exception handling patterns

---

## 🔄 Development Approach

1. **Start with Phase 1** - Get basic functionality working
2. **Refactor and learn** - Apply design patterns as you understand them
3. **Move to Phase 2** - Add features incrementally
4. **Optimize in Phase 3** - Focus on performance and advanced features

---

**Happy Learning! 🎬🎫**
