# Migration Guide: Raw JDBC → Spring Data JPA

## Step-by-Step Migration

### Step 1: Add Dependencies to `pom.xml`

Add these dependencies (after PostgreSQL dependency):

```xml
<!-- Spring Data JPA (includes Hibernate ORM) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

**What this gives you:**
- JPA (Java Persistence API) - the standard
- Hibernate - the implementation
- Spring Data JPA - Spring's abstraction layer

---

### Step 2: Update `application.yml`

Add JPA configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bms
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update  # Automatically create/update tables from entities
    show-sql: true       # Show SQL queries in console (for learning)
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

**What `ddl-auto: update` does:**
- Reads your `@Entity` classes
- Automatically creates/updates database tables
- No need for `schema.sql`!

---

### Step 3: Update User Entity (Add JPA Annotations)

**Current `User.java` (Simple POJO):**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String username;
    private String phone;
}
```

**Updated `User.java` (JPA Entity):**
```java
package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                    // Marks this as a database table
@Table(name = "users")     // Table name in database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id                                                    // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(unique = true, nullable = false)  // Unique and required
    private String email;
    
    @Column(nullable = false)                  // Required
    private String password;
    
    private String username;
    private String phone;
}
```

**Key Changes:**
- Added `@Entity` - tells JPA this is a database table
- Added `@Table(name = "users")` - maps to "users" table
- Added `@Id` - marks primary key
- Added `@GeneratedValue` - auto-increment ID
- Added `@Column` - column constraints

---

### Step 4: Create UserRepository Interface

**Create:** `src/main/java/com/repository/UserRepository.java`

```java
package com.repository;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring automatically generates implementation!
    // Method name "findByEmail" → SQL: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
    
    // You can add more methods:
    // Optional<User> findByUsername(String username);
    // List<User> findByUsernameContaining(String keyword);
}
```

**What `JpaRepository<User, Long>` means:**
- `User` = Entity type
- `Long` = Primary key type
- Provides: `save()`, `findById()`, `findAll()`, `delete()`, etc.

**Method Naming Magic:**
- `findByEmail` → `WHERE email = ?`
- `findByEmailAndPassword` → `WHERE email = ? AND password = ?`
- `findByUsernameContaining` → `WHERE username LIKE %?%`

---

### Step 5: Update LoginDao (Replace Raw JDBC with Repository)

**Current `LoginDao.java` (Raw JDBC - 80 lines):**
```java
@Repository
public class LoginDao {
    @Autowired
    private DatabaseConfig databaseConfig;
    
    public boolean validate(LoginDaoDTO req) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = databaseConfig.getConnection();
            String sql = "SELECT id, email, password FROM users WHERE email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, req.getEmail());
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String dbPassword = resultSet.getString("password");
                return dbPassword.equals(req.getPassword());
            }
            return false;
        } catch (SQLException e) {
            // ... error handling
        } finally {
            // ... close resources
        }
    }
}
```

**Updated `LoginDao.java` (Spring Data JPA - 15 lines!):**
```java
package com.repository;

import com.dto.LoginDaoDTO;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginDao {
    
    @Autowired
    private UserRepository userRepository;  // Inject repository instead of DatabaseConfig
    
    public boolean validate(LoginDaoDTO req) {
        // Find user by email (JPA handles all SQL!)
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
        
        if (userOpt.isEmpty()) {
            return false;  // User not found
        }
        
        User user = userOpt.get();
        
        // Compare passwords
        return user.getPassword().equals(req.getPassword());
    }
}
```

**What Changed:**
- ❌ Removed: Connection, PreparedStatement, ResultSet
- ❌ Removed: SQL queries
- ❌ Removed: Manual resource cleanup
- ✅ Added: UserRepository (Spring handles everything!)

---

### Step 6: Remove/Update Files

**Files to Remove:**
- `DatabaseConfig.java` - Not needed (Spring manages connections)
- `DatabaseInitializer.java` - Not needed (`ddl-auto: update` handles it)
- `schema.sql` - Not needed (JPA creates tables from entities)
- `data.sql` - Optional (can keep for initial data)

**Files to Keep:**
- `User.java` - Updated with JPA annotations
- `LoginDao.java` - Updated to use repository
- `application.yml` - Updated with JPA config

---

## How It Works (The Magic!)

### 1. **Entity Mapping:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;  // Maps to "id" column
}
```
→ JPA knows: "User class = users table, id field = id column"

### 2. **Repository Method:**
```java
Optional<User> findByEmail(String email);
```
→ Spring generates: `SELECT * FROM users WHERE email = ?`

### 3. **Automatic Mapping:**
```java
User user = userRepository.findByEmail("test@example.com");
```
→ JPA:
1. Executes SQL
2. Gets ResultSet
3. Creates User object
4. Maps columns to fields
5. Returns User object

**You never write SQL!**

---

## Comparison: Before vs After

| Aspect | Raw JDBC | Spring Data JPA |
|--------|----------|-----------------|
| **Lines of Code** | ~80 lines | ~15 lines |
| **SQL Writing** | Manual | Automatic |
| **Connection Management** | Manual | Automatic |
| **ResultSet Mapping** | Manual | Automatic |
| **Table Creation** | Manual SQL | Automatic from Entity |
| **Type Safety** | Runtime errors | Compile-time checking |

---

## What You'll Learn

1. **Entity Mapping:** How Java classes map to database tables
2. **JPA Annotations:** `@Entity`, `@Id`, `@Column`, etc.
3. **Repository Pattern:** Interface-based data access
4. **Method Naming:** How Spring generates queries from method names
5. **ORM Concepts:** Object-Relational Mapping in action

---

## Next Steps

1. Add `spring-boot-starter-data-jpa` dependency
2. Update `application.yml` with JPA config
3. Add JPA annotations to `User.java`
4. Create `UserRepository` interface
5. Update `LoginDao` to use repository
6. Remove old files (DatabaseConfig, DatabaseInitializer)
7. Test!

**The code will be much cleaner and easier to maintain!**

