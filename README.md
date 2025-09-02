<div align="center">

# 📚 Library System

Full‑stack monorepo: Spring Boot 3.5.5 backend (Java 24) + React 19 + Vite + Tailwind CSS frontend. Provides JWT secured REST API for users, books and simple loan (borrow) operations. Two run modes: Postgres (default) and instant in‑memory H2 (`dev` profile).

</div>

## ✨ Current Features

Backend
- JWT stateless auth (signup & login) with roles (`ROLE_USER`, `ROLE_ADMIN`)
- Seed users auto-created: `admin@example.com` / `user@example.com` (password: `password`)
- Book catalog: list (all authenticated), create (admin only)
- Borrow (loan) a book (marks it unavailable) – no return endpoint yet
- Pluggable datasource: PostgreSQL (default) or H2 in‑memory (`dev` profile)
- Simple layered structure with custom `@ApiRestController` meta‑annotation

Frontend
- React 19 + Vite + Tailwind CSS 4 scaffold (currently minimal UI placeholder)
- Build artifacts copied into Spring Boot static resources for a single deployable JAR

Security Caveats (Not Production Ready)
- Hard‑coded JWT secret & 10h token expiry in `JwtService`
- `ddl-auto: update` and no DB migrations (risk of drift)

## 🧱 Tech Stack Overview

| Area | Tech |
|------|------|
| Language | Java 24 |
| Backend | Spring Boot (Web, Security, Data JPA, Validation) |
| Auth | jjwt 0.13.0 (HMAC) |
| DB (default) | PostgreSQL |
| DB (dev) | H2 in‑memory + web console |
| Frontend | React 19, Vite 7, Tailwind CSS 4 |
| Build | Maven Wrapper (backend), Bun (scripts) / Vite / TypeScript |

## � Repository Structure

```
backend/        # Spring Boot application
frontend/       # React + Vite app (built assets copied into backend/static)
package.json    # Root scripts orchestrating dev/build (uses Bun, adaptable to npm)
```

Key packages (backend): `auth`, `books`, `loans`, `security`, `users`, `common`.

## 🚀 Quick Start (Dev, H2 In‑Memory)

Requirements: Java 24, (optional) Bun (or replace `bun` with `npm` / `pnpm`), Maven (wrapper included).

Option A: Monorepo concurrent dev (backend + frontend hot reload):

```bash
bun install        # installs root (none) & frontend deps via scripts if desired
bun run dev        # runs backend (H2) + frontend (Vite) concurrently
```

Vite dev server: http://localhost:5173. API: http://localhost:8080

Option B: Backend only (H2):

```bash
./backend/mvnw -f backend/pom.xml spring-boot:run -Pdev
```

## 🔐 Authentication Flow

1. (Optional) Sign up via `POST /auth/signup` or use seeded users.
2. Login `POST /auth/login` => `{ token: "<JWT>" }`.
3. Call protected endpoints with header: `Authorization: Bearer <JWT>`.

Token lifetime: 10 hours (see `JwtService`). Secret is hard‑coded (improve before production).

## ⚙️ Configuration (Postgres Default Profile)

Set env vars (Spring relaxed binding) to override defaults in `application.yml`:

| Purpose | Property | Example |
|---------|----------|---------|
| JDBC URL | `spring.datasource.url` | `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/library` |
| Username | `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME=postgres` |
| Password | `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD=secret` |
| Port | `server.port` | `SERVER_PORT=9090` |

Example run against local Postgres:

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/library"
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=password
./backend/mvnw -f backend/pom.xml spring-boot:run
```

### Dev (H2) Profile

```bash
./backend/mvnw -f backend/pom.xml spring-boot:run -Pdev
```
H2 Console: http://localhost:8080/h2-console  (JDBC: `jdbc:h2:mem:librarydb`)

## 🛣️ REST Endpoints (Current)

| Method | Path | Auth | Role | Notes |
|--------|------|------|------|-------|
| POST | /auth/signup | No | - | Registers user (ROLE_USER) |
| POST | /auth/login | No | - | Returns JWT token |
| GET | /api/books | Yes | USER/ADMIN | List books |
| POST | /api/books | Yes | ADMIN | Create book (rejects duplicate title+author) |
| POST | /api/loans/{bookId} | Yes | USER/ADMIN | Borrow book (fails if unavailable) |

No endpoint yet for returning/closing a loan.

## 🧪 Sample cURL

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
	-H 'Content-Type: application/json' \
	-d '{"email":"admin@example.com","password":"password"}' | jq -r .token)

curl -X POST http://localhost:8080/api/books \
	-H "Authorization: Bearer $TOKEN" \
	-H 'Content-Type: application/json' \
	-d '{"title":"Clean Architecture","author":"Robert C. Martin"}'

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/books
```

## 🏗️ Build for Deployment (Single JAR)

1. Build frontend & copy assets into backend static resources:
```bash
bun run build:frontend
```
2. Package backend JAR:
```bash
bun run build:backend
```
Resulting runnable JAR: `backend/target/library-system-0.0.1-SNAPSHOT.jar`

Run:
```bash
java -jar backend/target/library-system-0.0.1-SNAPSHOT.jar
```

## 🧭 Roadmap / Improvements

- Externalize JWT secret & move to env variable / Vault
- Loan return & overdue tracking
- Pagination / filtering for books
- Flyway (or Liquibase) migrations (replace `ddl-auto`)
- Dockerfile + docker-compose (Postgres + app)
- OpenAPI / Swagger docs
- Frontend: auth flow UI, book listing, borrow interactions
- Refresh tokens / revocation (blacklist or short access + refresh)

## 📝 License

MIT — see `LICENSE`.

---

Built with ☕ for learning & experimentation. Contributions / suggestions welcome.
