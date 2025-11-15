# ORM (Object-Relational Mapping) Explained

## What is ORM?

**ORM = Object-Relational Mapping**

It's a technique that lets you work with **Java objects** instead of writing **SQL queries** directly.

### Without ORM (Raw JDBC - What We Did):
```java
// You write SQL
String sql = "SELECT id, email, password FROM users WHERE email = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, email);
ResultSet rs = stmt.executeQuery();

// You manually map ResultSet to object
User user = new User();
if (rs.next()) {
    user.setId(rs.getLong("id"));
    user.setEmail(rs.getString("email"));
    user.setPassword(rs.getString("password"));
}
```

### With ORM (JPA/Hibernate):
```java
// You work with objects
User user = userRepository.findByEmail(email);
// That's it! JPA handles all SQL generation and mapping
```

---

## How ORM Mapping Works

### 1. **Entity Class** (Java Object)
```java
@Entity                    // Tells JPA: "This is a database table"
@Table(name = "users")    // Maps to "users" table
public class User {
    @Id                    // Primary key
    @GeneratedValue        // Auto-increment
    private Long id;
    
    @Column(name = "email")  // Maps to "email" column
    private String email;
    
    private String password;  // If column name matches, @Column optional
}
```

### 2. **JPA Annotations Explained**

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Entity` | Marks class as database table | `@Entity` |
| `@Table` | Specifies table name | `@Table(name = "users")` |
| `@Id` | Marks primary key field | `@Id` |
| `@GeneratedValue` | Auto-generate ID | `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| `@Column` | Maps field to column | `@Column(name = "email", unique = true)` |
| `@OneToMany` | One-to-many relationship | `@OneToMany(mappedBy = "user")` |
| `@ManyToOne` | Many-to-one relationship | `@ManyToOne` |

### 3. **Repository Interface** (No Implementation Needed!)
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring generates implementation automatically!
    Optional<User> findByEmail(String email);
    // Method name "findByEmail" → generates SQL: SELECT * FROM users WHERE email = ?
}
```

---

## Mapping Process

### Step-by-Step:

1. **You define Entity:**
   ```java
   @Entity
   public class User {
       @Id
       private Long id;
       private String email;
   }
   ```

2. **JPA reads annotations:**
   - Sees `@Entity` → knows it's a table
   - Sees `@Id` → knows it's primary key
   - Sees field names → maps to column names

3. **JPA generates SQL:**
   ```sql
   CREATE TABLE users (
       id BIGSERIAL PRIMARY KEY,
       email VARCHAR(255)
   );
   ```

4. **When you query:**
   ```java
   userRepository.findByEmail("test@example.com");
   ```
   
5. **JPA translates to SQL:**
   ```sql
   SELECT * FROM users WHERE email = 'test@example.com';
   ```

6. **JPA maps ResultSet to Object:**
   ```java
   User user = new User();
   user.setId(rs.getLong("id"));
   user.setEmail(rs.getString("email"));
   return user;
   ```

---

## Is Raw JDBC ORM?

**NO!** Raw JDBC is:
- Manual SQL writing
- Manual ResultSet mapping
- No object-relational mapping
- You do everything yourself

**JPA/Hibernate IS ORM:**
- Automatic SQL generation
- Automatic object mapping
- Works with objects, not SQL
- Framework does the work

---

## Benefits of ORM (JPA)

1. **Less Code:** No SQL writing
2. **Type Safety:** Compile-time checking
3. **Automatic Mapping:** No manual ResultSet handling
4. **Relationships:** Easy to define (OneToMany, ManyToOne)
5. **Database Agnostic:** Works with PostgreSQL, MySQL, etc.
6. **Automatic Table Creation:** Can create tables from entities

---

## When to Use What?

| Approach | When to Use |
|----------|-------------|
| **Raw JDBC** | Learning, maximum control, custom queries |
| **Spring JDBC** | Custom SQL but less boilerplate |
| **JPA/Hibernate** | Object-oriented, standard CRUD operations |
| **Spring Data JPA** | Most abstracted, fastest development |

---

## Next Steps

We'll migrate from Raw JDBC to Spring Data JPA to see the difference!

