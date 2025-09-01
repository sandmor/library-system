<div align="center">

# 📚 Library System

Lightweight Spring Boot 3 REST API for managing users, books and loaning operations with JWT authentication. Two run modes: a production‑like Postgres profile (default) and an instant in‑memory H2 dev profile.

</div>

## ✨ Features

- JWT based stateless authentication (signup & login)
- Role based authorization (`ROLE_USER`, `ROLE_ADMIN`)
- Book catalog (list, admin create)
- Borrow (loan) books (marks availability)
- Auto creation of seed users (`admin@example.com`, `user@example.com`, password: `password`)
- Switchable datasource: Postgres (default) or in‑memory H2 (`dev` profile)
- Java 24 + Spring Boot 3.5.5

## 🧱 Tech Stack

| Layer | Tech |
|-------|------|
| Runtime | Java 24 |
| Framework | Spring Boot (Web, Security, Data JPA, Validation) |
| Auth | JWT (jjwt 0.13.0) |
| DB (default) | PostgreSQL |
| DB (dev profile) | H2 in‑memory + web console |
| Build | Maven Wrapper |

## 🚀 Quick Start

Clone and run with in‑memory DB (no external services needed):

```bash
./backend/mvnw -f backend/pom.xml spring-boot:run -Pdev
```

Then call a secured endpoint (after obtaining a token) at: `http://localhost:8080`.

## 🔐 Authentication Flow

1. Sign up (or use seeded users) via `POST /auth/signup`.
2. Login via `POST /auth/login` to receive a JWT.
3. Send `Authorization: Bearer <token>` on subsequent requests.

Token lifetime: 10 hours (hard‑coded in `JwtService`).

## ⚙️ Configuration

Default profile (no explicit `spring.profiles.active`) targets PostgreSQL. These properties reside in `application.yml` but are overrideable via environment variables (Spring Boot relaxed binding):

| Purpose | Property | Env Var Example |
|---------|----------|-----------------|
| JDBC URL | `spring.datasource.url` | `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/library` |
| Username | `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME=postgres` |
| Password | `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD=secret` |
| Port | `server.port` | `SERVER_PORT=9090` |

### Example: Run against local Postgres

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/library"
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=password
./backend/mvnw -f backend/pom.xml spring-boot:run
```

### Dev (H2) profile

Uses `application-dev.yml` with an in‑memory H2 database and SQL logging.

```bash
./backend/mvnw -f backend/pom.xml spring-boot:run -Pdev
```

Access H2 console (if exposed) at: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:librarydb`).

## 🛣️ REST Endpoints

| Method | Path | Auth | Role | Description |
|--------|------|------|------|-------------|
| POST | `/auth/signup` | No | - | Register new user (role USER) |
| POST | `/auth/login` | No | - | Obtain JWT token |
| GET | `/books` | Yes | USER/ADMIN | List all books |
| POST | `/books` | Yes | ADMIN | Create a new book |
| POST | `/loans/{bookId}` | Yes | USER/ADMIN | Borrow a book (fails if unavailable) |

## 🧪 Sample cURL Session

```bash
# 1. Login as admin
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
	-H 'Content-Type: application/json' \
	-d '{"email":"admin@example.com","password":"password"}' | jq -r .token)

# 2. Create a book
curl -X POST http://localhost:8080/books \
	-H "Authorization: Bearer $TOKEN" \
	-H 'Content-Type: application/json' \
	-d '{"title":"Clean Architecture","author":"Robert C. Martin"}'

# 3. List books (using same token)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/books
```

## 🧭 Roadmap Ideas

- Make the JWT secret an environment variable
- Return (close) a loan & overdue tracking
- Pagination & search for books
- Flyway migrations
- Refresh tokens / token revocation
- Dockerfile + docker-compose (Postgres + app)
- OpenAPI / Swagger documentation

## 📝 License

See `LICENSE`.

---

Built with ☕ by exploring simple, testable building blocks. Enjoy hacking! 
