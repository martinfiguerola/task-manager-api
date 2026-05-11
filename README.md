# Task Manager API

REST API for task management with user authentication built with Java and Spring Boot.

## 🚀 Live Demo

**Base URL:** https://task-manager-api-production-6511.up.railway.app

### Auth
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /auth/register | Register a new user | ❌ |
| POST | /auth/login | Login and get JWT token | ❌ |

### Tasks
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | /api/tasks | Get all tasks (paginated, filterable by status) | ✅ |
| GET | /api/tasks/{id} | Get task by ID | ✅ |
| POST | /api/tasks | Create a new task | ✅ |
| PUT | /api/tasks/{id} | Update a task | ✅ |
| DELETE | /api/tasks/{id} | Delete a task | ✅ |

### Users
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | /api/users | Get all users | ✅ |
| GET | /api/users/{id} | Get user by ID | ✅ |
| POST | /api/users | Create a new user | ✅ |
| PUT | /api/users/{id} | Update a user | ✅ |
| DELETE | /api/users/{id} | Delete a user | ✅ |

## 🛠️ Tech Stack

- **Java 17** + **Spring Boot 3**
- **Spring Security** + **JWT** authentication
- **Spring Data JPA** + **Hibernate**
- **MySQL**
- **Docker** + **Docker Compose**
- **Maven**
- **JUnit** + **Mockito**

## ✨ Features

- User registration and login with JWT authentication
- Full CRUD operations for tasks and users
- Task filtering by status and pagination
- Secure endpoints — all task and user routes require valid JWT
- Input validation
- Containerized with Docker

## 🏃 Run locally with Docker

1. Clone the repository
```bash
   git clone https://github.com/martinfiguerola/task-manager-api.git
   cd task-manager-api
```

2. Create a `.env` file based on the example
```bash
   cp .env.example .env
```

3. Start the application
```bash
   docker-compose up
```

The API will be available at `http://localhost:8080`

## 📋 Example usage

**Register:**
```bash
curl -X POST https://task-manager-api-production-6511.up.railway.app/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "12345678"}'
```

**Login:**
```bash
curl -X POST https://task-manager-api-production-6511.up.railway.app/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "12345678"}'
```

**Get tasks:**
```bash
curl https://task-manager-api-production-6511.up.railway.app/api/tasks \
  -H "Authorization: Bearer <your_token>"
```

**Get tasks filtered by status:**
```bash
curl "https://task-manager-api-production-6511.up.railway.app/api/tasks?status=IN_PROGRESS&page=0&size=10" \
  -H "Authorization: Bearer <your_token>"
```

## 🧪 Testing

```bash
./mvnw test
```

14 unit tests covering service layer with JUnit and Mockito.

## 📦 Deployment

Deployed on **Railway** with a MySQL database. Auto-deploys on push to `main`.
