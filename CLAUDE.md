# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4 (Java 17, Maven) REST API for per-user task management with JWT auth. Deployed on Railway (PostgreSQL), auto-deploys on push to `main`. Swagger UI at `/swagger-ui/index.html`.

## Commands

```bash
./mvnw test                                              # run all tests (what CI runs on push to main)
./mvnw test -Dtest=TaskServiceImplTest                   # single test class
./mvnw test -Dtest=TaskServiceImplTest#methodName        # single test method
./mvnw clean package -DskipTests                         # build jar (used by Dockerfile)
docker-compose up                                        # app + MySQL 8 locally; needs .env (cp .env.example .env)
```

Running outside Docker requires env vars `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, and optionally `SPRING_PROFILES_ACTIVE` (`mysql` default, `postgres` in production) and `PORT`.

Tests are pure Mockito unit tests of the `service/impl` classes — no Spring context or database needed. They fake the logged-in user by mocking `SecurityContextHolder` in `@BeforeEach`.

## Architecture

Layered: `controller` → `service` (interfaces) / `service/impl` → `repository` (Spring Data JPA). DTOs are Java records under `dto/{auth,task,user}`; entity↔DTO conversion uses MapStruct (`mapper/`, `componentModel = "spring"`). Lombok and MapStruct both run as annotation processors configured in `pom.xml`.

**Auth flow:** `JwtFilter` (before `UsernamePasswordAuthenticationFilter`) reads `Authorization: Bearer <token>`, extracts the email subject via `JwtUtil`, loads the user, and sets the `SecurityContext`. Sessions are stateless; only `POST /auth/**` and Swagger paths are public. The `User` entity itself implements `UserDetails` (username = email, authority = `Role` name, e.g. `ROLE_ADMIN`). Registration always assigns `ROLE_USER`; there is no API path to create admins.

**Ownership enforcement:** Task services never take a user id from the request. They read the current email from `SecurityContextHolder` and query with `findByUserEmail…` / `findByIdAndUserEmail`, so another user's task simply yields `TaskNotFoundException` (404). Preserve this pattern for any new task queries.

**Authorization:** `UserController` is class-level `@PreAuthorize("hasRole('ADMIN')")` (enabled by `@EnableMethodSecurity`).

**Business rules (in `TaskServiceImpl` / `UserServiceImpl`):**
- `Status` transitions only move forward (`PENDING → IN_PROGRESS → DONE`) via `Status.canTransitionTo` (ordinal comparison); `DONE` tasks are immutable.
- `TaskUpdateDTO.status` is a String parsed manually so bad values produce `InvalidTaskStatusException` (400).
- A user with any tasks cannot be deleted (`UserHasActiveTasksException`).

**Errors:** All errors are RFC 7807 `ProblemDetail` responses from `GlobalExceptionHandler`. New custom exceptions must be registered there — it groups them by status (404 not-found group, 409 "Business Rule Violation" group). An invalid `sort` query param surfaces as `InvalidDataAccessApiUsageException` and is mapped to 400.

## Configuration notes

- `application.yaml` holds shared config (`ddl-auto: update` — no migration tool; schema is driven by entities). `application-mysql.yml` / `application-postgres.yml` only swap datasource driver/dialect.
- The postgres profile sets `server.forward-headers-strategy: framework` so Swagger generates `https` URLs behind Railway's proxy.
- JWT expiration is hardcoded in `application.yaml` (`jwt.expiration`); the `JWT_EXPIRATION` var in `.env.example` is not currently read.
