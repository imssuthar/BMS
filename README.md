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

## 📋 Product Requirements

### **Phase 1: Core Booking System (MVP)**

**Goal:** Build a basic movie ticket booking system with essential features.

#### Functional Requirements:

1. **User Management**
   - User registration and login
   - User profile management
   - Basic authentication

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

#### Technical Learning Focus:
- **Design Patterns:** Factory, Builder, Singleton, Strategy
- **Architecture:** Layered Architecture (Controller → Service → Repository)
- **LLD:** Entity relationships, data modeling
- **Concepts:** Separation of concerns, dependency injection

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

#### Technical Learning Focus:
- **Design Patterns:** Observer, Command, State, Template Method
- **Architecture:** Service-Oriented Architecture, Event-Driven patterns
- **Concurrency:** Locking mechanisms, race conditions, thread safety
- **Concepts:** Caching strategies, transaction management, optimistic/pessimistic locking

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

#### Technical Learning Focus:
- **Design Patterns:** Facade, Adapter, Decorator, Chain of Responsibility
- **Architecture:** Microservices concepts, API Gateway pattern, CQRS
- **Performance:** Database indexing, query optimization, caching strategies
- **Concepts:** Load balancing, horizontal scaling, distributed systems basics

---

## 🏗️ Architecture Guidelines

### Phase 1 Structure:
```
com.bms
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entities/DTOs
├── util/           # Utility classes
└── exception/      # Custom exceptions
```

### Design Principles to Follow:
1. **SOLID Principles**
2. **DRY (Don't Repeat Yourself)**
3. **Separation of Concerns**
4. **Dependency Inversion**
5. **Single Responsibility**

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

## 🚀 Getting Started

### Prerequisites:
- Java 17+
- Maven 3.6+
- Spring Boot 3.2.0
- (Optional) Database: H2 (in-memory) or PostgreSQL

### Running the Project:
```bash
# Build the project
mvn clean package

# Run the application
java -jar target/BMS-1.0-SNAPSHOT.jar

# Or use Maven
mvn spring-boot:run
```

### API Endpoints:
- Base URL: `http://localhost:8080`
- Health Check: `GET /api/status`

---

## 📝 Notes

- **Focus on learning**, not perfection
- **Ask for suggestions** when stuck on design decisions
- **Implement incrementally** - one feature at a time
- **Refactor as you learn** - it's okay to improve previous code
- **Document your decisions** - why you chose a particular pattern/approach

---

## 🔄 Development Approach

1. **Start with Phase 1** - Get basic functionality working
2. **Refactor and learn** - Apply design patterns as you understand them
3. **Move to Phase 2** - Add features incrementally
4. **Optimize in Phase 3** - Focus on performance and advanced features

---

## 📚 Resources for Learning

- Design Patterns: Gang of Four (GoF) patterns
- System Design: High-level and low-level design principles
- Spring Boot: Official documentation for implementation
- Database Design: Normalization, indexing, relationships

---

**Happy Learning! 🎬🎫**

