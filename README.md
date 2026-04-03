# SocialConnect-Backend-System

Scalable REST API backend for a social matching platform built with Java 17 and Spring Boot 3.

## Core Capabilities

- User profile management (CRUD)
- User preference management
- Interaction tracking (LIKE, SKIP, MATCH)
- Activity feed pipeline with pagination
- Recommendation pipeline placeholder based on preferences/interests
- AWS S3 integration for profile picture asset storage
- DTO-first API design with validation
- Centralized exception handling with standardized error responses
- Flyway-based schema evolution and controlled seed data

## Tech Stack

- Java 17
- Spring Boot 3.3.x
- Spring Web
- Spring Data JPA + Hibernate
- MySQL 8+
- Flyway
- AWS SDK v2 (S3)
- Maven

## Architecture

This service follows a modular N-tier structure:

- Controller layer: HTTP routing and request/response shaping
- Service layer: business logic and orchestration
- Repository layer: persistence abstraction via Spring Data JPA
- Entity layer: relational domain model
- DTO layer: strict input/output contracts to avoid exposing entities
- Exception layer: consistent API error handling via `@RestControllerAdvice`

## Project Structure

```text
src/main/java/com/socialconnect/backend
	config/          # S3 and configuration properties
	controller/      # REST endpoints
	dto/             # Request/response models
	entity/          # JPA entities
	exception/       # Custom exceptions + global handler
	repository/      # Spring Data repositories
	service/         # Business services
		storage/       # Storage abstraction + S3 implementation

src/main/resources
	application.yml
	db/migration/
		V1__create_core_schema.sql
		R__seed_initial_data.sql
```

## Domain Model

- `users`
	- id, username, email, bio, profile_picture_url, created_at, updated_at
- `user_preferences`
	- user_id relation, target age range, location, interests
- `interactions`
	- actor user, target user, type, created_at

Constraints are enforced through unique keys, foreign keys, check constraints, and indexes.

## Prerequisites

- JDK 17+
- Maven 3.9+
- MySQL 8+
- AWS credentials source (IAM role preferred)

## Quick Start

### 1. Configure environment variables

```bash
export DB_URL='jdbc:mysql://localhost:3306/socialconnect?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export DB_USERNAME='social_user'
export DB_PASSWORD='social_password'
export AWS_REGION='us-east-1'
export AWS_S3_BUCKET='socialconnect-assets'
```

Optional for local seeding:

```bash
export DB_SEED_ENABLED=true
```

### 2. Build and run

```bash
mvn clean spring-boot:run
```

Flyway migrations run automatically at startup.

## Configuration Reference

| Variable | Default | Purpose |
|---|---|---|
| PORT | 8080 | HTTP port |
| DB_URL | jdbc:mysql://localhost:3306/socialconnect... | MySQL JDBC URL |
| DB_USERNAME | social_user | Database username |
| DB_PASSWORD | social_password | Database password |
| DB_POOL_MIN_IDLE | 5 | Hikari minimum idle connections |
| DB_POOL_MAX_SIZE | 25 | Hikari maximum pool size |
| DB_POOL_IDLE_TIMEOUT_MS | 300000 | Idle timeout |
| DB_POOL_MAX_LIFETIME_MS | 1800000 | Connection max lifetime |
| DB_POOL_CONNECTION_TIMEOUT_MS | 30000 | Connection acquire timeout |
| DB_POOL_LEAK_DETECTION_MS | 20000 | Leak detection threshold |
| JPA_DDL_AUTO | validate | Hibernate schema mode (use Flyway for DDL) |
| JPA_SHOW_SQL | false | SQL logging |
| FLYWAY_ENABLED | true | Flyway migration toggle |
| DB_SEED_ENABLED | false | Enables repeatable seed migration |
| AWS_REGION | us-east-1 | AWS region |
| AWS_S3_BUCKET | socialconnect-assets | S3 bucket for profile assets |

## Flyway Migration Strategy

Migrations are in `src/main/resources/db/migration`.

- Versioned migration: `V1__create_core_schema.sql`
	- Creates core tables, constraints, and indexes
- Repeatable migration: `R__seed_initial_data.sql`
	- Inserts/upserts bootstrap data only when `DB_SEED_ENABLED=true`

Operational defaults:

- `spring.flyway.validate-on-migrate=true`
- `spring.flyway.baseline-on-migrate=true`
- `spring.flyway.clean-disabled=true`
- `spring.jpa.hibernate.ddl-auto=validate`

### Adding a new migration

Create a new versioned file, for example:

```text
src/main/resources/db/migration/V2__add_user_last_active_at.sql
```

Use forward-only SQL changes suitable for production rollouts.

## API Overview

Base path: `/api/v1`

### User Profiles

| Method | Endpoint | Description |
|---|---|---|
| POST | /users | Create user profile |
| GET | /users/{id} | Get user profile by id |
| GET | /users?page=0&size=20 | List users (paginated) |
| PUT | /users/{id} | Update user profile |
| DELETE | /users/{id} | Delete user profile |
| POST | /users/{id}/profile-picture | Upload profile picture (multipart `file`) |
| DELETE | /users/{id}/profile-picture?key=... | Delete profile picture object from S3 |

### Preferences

| Method | Endpoint | Description |
|---|---|---|
| PUT | /users/{userId}/preferences | Create or update preferences |
| GET | /users/{userId}/preferences | Get preferences |

### Interactions

| Method | Endpoint | Description |
|---|---|---|
| POST | /interactions | Create interaction |
| GET | /users/{userId}/interactions?page=0&size=20 | List interactions (paginated) |

### Feed and Recommendations

| Method | Endpoint | Description |
|---|---|---|
| GET | /users/{userId}/feed?page=0&size=20 | Fetch activity feed |
| GET | /users/{userId}/recommendations?page=0&size=20 | Fetch recommendation candidates |

## Request Validation

Input validation uses `jakarta.validation` on DTOs.

Examples:

- username length boundaries
- email format checks
- positive identifiers for interactions
- preference age range validation

## Error Response Contract

All errors are normalized through global exception handling.

Example:

```json
{
	"timestamp": "2026-04-03T12:34:56.789Z",
	"status": 400,
	"error": "Bad Request",
	"message": "Validation failed",
	"path": "/api/v1/users",
	"validationErrors": {
		"email": "Email format is invalid"
	}
}
```

## AWS S3 Integration

Storage behavior is implemented through a storage abstraction and S3-backed service.

- Upload object
- Retrieve object bytes
- Delete object
- Build object URL

Credential resolution is IAM-first via AWS SDK default provider chain.

## Health and Observability

Actuator endpoints exposed:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`
- `/actuator/prometheus`

## Common Commands

```bash
# Compile
mvn -DskipTests compile

# Run tests
mvn test

# Run app
mvn spring-boot:run
```
