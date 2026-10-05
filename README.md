# Task Manager API

A REST API for task management, built with Java and Spring Boot, featuring JWT authentication, ownership-based authorization, and role-based access control. Deployed live on Railway with interactive API documentation.

![CI](https://github.com/martinfiguerola/task-manager-api/actions/workflows/ci.yml/badge.svg)

## Live Demo

**Base URL:** https://task-manager-api-production-6511.up.railway.app

See [API Documentation](#api-documentation-swagger) below to try it interactively.

## Highlights

- **Ownership-based security** — users can only access their own tasks, enforced at the repository layer, not just the controller
- **Role-based authorization** — separate admin-only endpoints for user management, backed by Spring Security's `@PreAuthorize`
- **RFC 7807 error handling** — consistent, structured error responses (`ProblemDetail`) across the entire API, including custom handlers for authentication failures and invalid query parameters
- **22 unit tests** covering services (Task, User, Auth) with JUnit and Mockito
- **CI/CD pipeline** with GitHub Actions — tests run automatically on every push
- **Multi-database support** (MySQL for local dev, PostgreSQL for production) via Spring Profiles, with zero code changes

## API Documentation (Swagger)

Interactive API docs, available to try directly in the browser:

https://task-manager-api-production-6511.up.railway.app/swagger-ui/index.html

To test protected endpoints:
1. Log in with the demo account below (or register your own) via `POST /auth/login`, and copy the `token` from the response.
2. Click **Authorize** (top right) and paste the token.
3. Try any endpoint — the token is sent automatically.

Demo account (already has sample tasks):
```
Email: demo@taskmanager.com
Password: Demo1234
```

## API Endpoints

### Auth
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /auth/register | Register a new user | ❌ |
| POST | /auth/login | Login and get JWT token | ❌ |

### Tasks
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | /api/tasks | Get all tasks (paginated, filterable by status, sortable) | ✅ |
| GET | /api/tasks/{id} | Get task by ID | ✅ |
| POST | /api/tasks | Create a new task | ✅ |
| PUT | /api/tasks/{id} | Update a task | ✅ |
| DELETE | /api/tasks/{id} | Delete a task | ✅ |

### Users (Admin only)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | /api/users | Get all users | 🔒 Admin |
| GET | /api/users/{id} | Get user by ID | 🔒 Admin |
| PUT | /api/users/{id} | Update a user | 🔒 Admin |
| DELETE | /api/users/{id} | Delete a user | 🔒 Admin |

## Tech Stack

- Java 17 + Spring Boot 4
- Spring Security + JWT authentication
- Spring Data JPA + Hibernate
- MySQL and PostgreSQL support via Spring Profiles
- Docker + Docker Compose
- Maven
- JUnit + Mockito
- Swagger/OpenAPI (springdoc)
- GitHub Actions (CI/CD)


## Architecture

The project follows a layered architecture:

- **Controller** → receives HTTP requests and delegates to the service layer
- **Service** → contains the business logic
- **Repository** → handles database access via Spring Data JPA, enforcing ownership at the query level
- **Security** → JWT filter intercepts every request and validates the token before it reaches the controller
- **DTOs** → separate the internal model from the API response
- **Global exception handling** → RFC 7807 (`ProblemDetail`) responses for validation errors, authentication failures, and invalid parameters

The application supports multiple database environments (MySQL and PostgreSQL) through Spring Profiles, allowing the same codebase to run against different database engines without code changes.

## Run Locally with Docker

1. Clone the repository
```
   git clone https://github.com/martinfiguerola/task-manager-api.git
   cd task-manager-api
```

2. Create a .env file based on the example
```
   cp .env.example .env
```

3. Start the application
```
   docker-compose up
```

The API will be available at http://localhost:8080

## Example Usage

Register:
```
curl -X POST https://task-manager-api-production-6511.up.railway.app/auth/register \
-H "Content-Type: application/json" \
-d '{"email": "user@example.com", "password": "12345678"}'
```

Login:
```
curl -X POST https://task-manager-api-production-6511.up.railway.app/auth/login \
-H "Content-Type: application/json" \
-d '{"email": "user@example.com", "password": "12345678"}'
```

Get tasks:
```
curl https://task-manager-api-production-6511.up.railway.app/api/tasks \
-H "Authorization: Bearer <your_token>"
```

Get tasks filtered by status:
```
curl "https://task-manager-api-production-6511.up.railway.app/api/tasks?status=IN_PROGRESS&page=0&size=10" \
-H "Authorization: Bearer <your_token>"
```

## 🧪 Testing
```
./mvnw test
```

22 unit tests covering the service layer (Task, User, Auth) with JUnit and Mockito.

## 📦 Deployment

Deployed on Railway with a PostgreSQL database in production.
The project also supports MySQL for local development via Spring Profiles.
Auto-deploys on push to main.
