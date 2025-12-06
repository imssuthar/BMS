# Docker Setup Guide for BookMyShow

## Quick Start

### 1. Build and Start All Services

```bash
# Build and start PostgreSQL + Java app
docker-compose up -d --build

# View logs
docker-compose logs -f app
```

### 2. Access the Application

- **Application:** http://localhost:8080
- **PostgreSQL:** localhost:5432

### 3. Stop Services

```bash
# Stop all services (keeps data)
docker-compose stop

# Stop and remove containers (keeps data volume)
docker-compose down

# Stop and remove everything (including data)
docker-compose down -v
```

---

## Docker Commands

### Build Only
```bash
docker-compose build
```

### Start Services
```bash
docker-compose up -d
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f postgres
```

### Restart Service
```bash
docker-compose restart app
```

### Rebuild After Code Changes
```bash
docker-compose up -d --build app
```

---

## Environment Variables

You can override configuration using environment variables:

### Database (in docker-compose.yml)
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password

### SendGrid (in docker-compose.yml)
- `EMAIL_SENDGRID_API_KEY` - SendGrid API key
- `EMAIL_SENDGRID_FROM_EMAIL` - Verified sender email

### Using .env file (Optional)

Create a `.env` file in project root:

```env
SENDGRID_API_KEY=your-api-key-here
SENDGRID_FROM_EMAIL=your-email@example.com
```

Then update `docker-compose.yml` to use:
```yaml
EMAIL_SENDGRID_API_KEY: ${SENDGRID_API_KEY}
EMAIL_SENDGRID_FROM_EMAIL: ${SENDGRID_FROM_EMAIL}
```

---

## Architecture

```
┌─────────────────┐
│   Docker Host   │
│                 │
│  ┌───────────┐  │
│  │   App     │  │  :8080
│  │ (Java)    │──┼──► http://localhost:8080
│  └─────┬─────┘  │
│        │        │
│        │        │
│  ┌─────▼─────┐  │
│  │ PostgreSQL│  │  :5432
│  │           │──┼──► localhost:5432
│  └───────────┘  │
│                 │
└─────────────────┘
```

---

## Troubleshooting

### App Can't Connect to Database
- Check if postgres service is healthy: `docker-compose ps`
- Wait for postgres to be ready: `docker-compose logs postgres`
- Verify network: Both services should be on `bms-network`

### Port Already in Use
```bash
# Check what's using the port
lsof -i :8080
lsof -i :5432

# Change ports in docker-compose.yml if needed
```

### Rebuild After Code Changes
```bash
# Rebuild and restart
docker-compose up -d --build app
```

### View Application Logs
```bash
docker-compose logs -f app
```

### Access Container Shell
```bash
# Java app container
docker exec -it bms-app sh

# PostgreSQL container
docker exec -it bms-postgres psql -U postgres -d bms
```

---

## Development vs Production

### Development (Current Setup)
- Uses local Docker volumes
- Logs visible in console
- Auto-rebuild on changes

### Production Recommendations
- Use Docker secrets for sensitive data
- Add health checks
- Use Docker Swarm or Kubernetes
- Set up proper logging (ELK stack)
- Use reverse proxy (Nginx)
- Enable HTTPS

---

## Health Checks

The app includes a health check endpoint:
```bash
curl http://localhost:8080/actuator/health
```

Note: You may need to add Spring Boot Actuator dependency for this to work.

---

## Next Steps

1. **Add Actuator** (for health checks):
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

2. **Add .env file** for sensitive configuration

3. **Set up CI/CD** to build and deploy Docker images

4. **Add monitoring** (Prometheus, Grafana)

---

**Happy Dockerizing! 🐳**
