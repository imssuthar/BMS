# Docker PostgreSQL Setup Guide

## Quick Start

### 1. Start PostgreSQL Container

```bash
# Start PostgreSQL in Docker
docker-compose up -d

# Check if container is running
docker ps

# View logs
docker-compose logs -f postgres
```

### 2. Verify Database is Ready

```bash
# Check container health
docker-compose ps

# Connect to database (optional)
docker exec -it bms-postgres psql -U postgres -d bms
```

### 3. Run Your Application

```bash
mvn spring-boot:run
```

The application will automatically:
- Connect to PostgreSQL in Docker
- Run `schema.sql` to create tables
- Run `data.sql` to insert test data

---

## Docker Commands

### Start PostgreSQL
```bash
docker-compose up -d
```

### Stop PostgreSQL (keeps data)
```bash
docker-compose stop
```

### Stop and Remove Container (keeps data volume)
```bash
docker-compose down
```

### Stop and Remove Everything (including data)
```bash
docker-compose down -v
```

### View Logs
```bash
docker-compose logs -f postgres
```

### Access PostgreSQL CLI
```bash
docker exec -it bms-postgres psql -U postgres -d bms
```

---

## Database Connection Details

- **Host:** localhost
- **Port:** 5432
- **Database:** bms
- **Username:** postgres
- **Password:** postgres

These match your `application.yml` configuration.

---

## Troubleshooting

### Port Already in Use
If port 5432 is already in use:
1. Change port in `docker-compose.yml`: `"5433:5432"`
2. Update `application.yml`: `jdbc:postgresql://localhost:5433/bms`

### Container Won't Start
```bash
# Check logs
docker-compose logs postgres

# Remove and recreate
docker-compose down -v
docker-compose up -d
```

### Data Persistence
Data is stored in Docker volume `postgres_data`. Even if you remove the container, data persists.

To completely remove data:
```bash
docker-compose down -v
```

---

## Verify Setup

After starting the container, test the connection:

```bash
# From your application
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

---

## Production Note

For production, change the default password in:
- `docker-compose.yml` (POSTGRES_PASSWORD)
- `application.yml` (spring.datasource.password)

