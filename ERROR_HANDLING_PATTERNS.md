# Error Handling Patterns - Industry Best Practices

## 🎯 Overview

This document explains the error handling patterns implemented in the BMS application, following industry best practices.

---

## 📋 Patterns Implemented

### 1. **Custom Exception Hierarchy**

```
BMSException (Base)
├── ResourceNotFoundException (404)
├── ValidationException (400)
├── UnauthorizedException (401)
├── ForbiddenException (403)
└── ConflictException (409)
```

**Benefits:**
- Type-safe error handling
- Clear error categorization
- Easy to extend for new error types

---

### 2. **Global Exception Handler** (`@RestControllerAdvice`)

**Location:** `GlobalExceptionHandler.java`

**What it does:**
- Catches all exceptions from controllers
- Converts them to standardized error responses
- Provides consistent error format across the API

**Benefits:**
- Centralized error handling
- No duplicate error handling code
- Consistent API responses

---

### 3. **Standardized Error Response**

**DTO:** `ErrorResponse.java`

```json
{
  "errorCode": "RESOURCE_NOT_FOUND",
  "message": "User with identifier 'test@example.com' not found",
  "statusCode": 404,
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/delete-account"
}
```

**Benefits:**
- Consistent error format
- Includes error code for client-side handling
- Timestamp for debugging
- Request path for tracing

---

### 4. **Exception-Driven Flow**

**Before (Old Pattern):**
```java
if (user == null) {
    return createErrorResponse(404, "User not found");
}
```

**After (New Pattern):**
```java
User user = userRepository.findByEmail(email)
    .orElseThrow(() -> new ResourceNotFoundException("User", email));
```

**Benefits:**
- Cleaner code (no if-else error handling)
- Exceptions bubble up automatically
- Service layer focuses on business logic

---

## 🔄 How It Works

### Flow Diagram:

```
Controller
    ↓
Service (throws exception)
    ↓
GlobalExceptionHandler (catches exception)
    ↓
ErrorResponse (standardized format)
    ↓
HTTP Response (JSON)
```

### Example:

1. **Service throws exception:**
   ```java
   throw new ResourceNotFoundException("User", email);
   ```

2. **Global handler catches it:**
   ```java
   @ExceptionHandler(BMSException.class)
   public ResponseEntity<ErrorResponse> handleBMSException(...)
   ```

3. **Returns standardized response:**
   ```json
   {
     "errorCode": "RESOURCE_NOT_FOUND",
     "message": "User with identifier 'test@example.com' not found",
     "statusCode": 404,
     "timestamp": "2024-01-15T10:30:00"
   }
   ```

---

## 📚 Exception Types & Usage

### 1. **ResourceNotFoundException** (404)
**When to use:** Resource doesn't exist
```java
throw new ResourceNotFoundException("User", email);
// or
throw new ResourceNotFoundException("User not found");
```

### 2. **ValidationException** (400)
**When to use:** Business logic validation fails
```java
throw new ValidationException("Email already exists");
// or
throw new ValidationException("email", "must be unique");
```

### 3. **UnauthorizedException** (401)
**When to use:** Authentication fails
```java
throw new UnauthorizedException("Invalid or expired token");
```

### 4. **ForbiddenException** (403)
**When to use:** Authorization fails (user authenticated but not authorized)
```java
throw new ForbiddenException("Token does not match user account");
```

### 5. **ConflictException** (409)
**When to use:** Resource conflict (e.g., duplicate email)
```java
throw new ConflictException("Email", email);
// or
throw new ConflictException("Email already exists");
```

---

## 🎨 Validation Error Handling

The global handler automatically handles:
- `@Valid` annotation errors → `MethodArgumentNotValidException`
- Constraint violations → `ConstraintViolationException`

**Example Response:**
```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Validation failed: email: must not be blank, password: size must be between 4 and 30",
  "statusCode": 400,
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 🔧 Migration Guide

### Step 1: Replace error returns with exceptions

**Before:**
```java
if (user == null) {
    return createErrorResponse(404, "User not found");
}
```

**After:**
```java
User user = userRepository.findByEmail(email)
    .orElseThrow(() -> new ResourceNotFoundException("User", email));
```

### Step 2: Remove `createErrorResponse` helper methods

These are no longer needed - exceptions handle errors automatically.

### Step 3: Update controllers

Controllers don't need to handle errors - just let exceptions bubble up:
```java
@DeleteMapping("/delete-account")
public ResponseEntity<Object> deleteAccount(...) {
    // No try-catch needed!
    LoginResponse resp = deleteAccountService.deleteAccount(token);
    return ResponseEntity.ok(resp.getData());
}
```

---

## ✅ Benefits

1. **Cleaner Code:**
   - No more `if (error) return error` patterns
   - Service methods focus on business logic

2. **Consistency:**
   - All errors follow same format
   - Predictable API responses

3. **Maintainability:**
   - Centralized error handling
   - Easy to add new error types

4. **Type Safety:**
   - Compile-time error checking
   - IDE autocomplete support

5. **Separation of Concerns:**
   - Services: Business logic
   - Controllers: Request/response handling
   - Exception Handler: Error formatting

---

## 🚀 Next Steps

1. **Migrate other services** to use exceptions
2. **Add logging** (use SLF4J/Logback instead of System.err)
3. **Add error codes enum** for better type safety
4. **Add request ID** for distributed tracing
5. **Add error details** for debugging (in development mode)

---

## 📖 Industry References

- **Spring Boot Best Practices:** Global exception handling
- **REST API Design:** Standardized error responses
- **Clean Code:** Exception-driven error handling
- **Domain-Driven Design:** Domain exceptions

---

**Happy Error Handling! 🎯**

