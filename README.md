# Expenses API (using JWT)

REST API for expense management developed with Spring Boot.

## Features
- JWT Authentication: Security based on JWT tokens
- User Management: Registration and login
- Categories: Organization of expenses by categories
- Expenses: Complete CRUD for personal expenses
- Validation: Robust validation of input data
- Documentation: API documented with OpenAPI/Swagger

## Technologies
- Java 21
- Spring Boot 3.5.3
- Spring Security (JWT Authentication)
- Spring Data JPA
- PostgreSQL
- Lombok
- SpringDoc OpenAPI

## API docs

Once the application is started, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

# Execute application

## Authentication

### Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan@ejemplo.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@ejemplo.com",
    "password": "password123"
  }'
```

### Using the Token

```bash
curl -X GET http://localhost:8080/api/expenses \
  -H "Authorization: Bearer your_jwt_token_here"
```

## Usage Examples

### Create a Category

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer your_jwt_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Food",
    "description": "Expenses related to meals"
  }'
```

## Create an Expense

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Authorization: Bearer your_jwt_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "expenseDate": "2024-01-15T12:30:00",
    "categoryName": "Food",
    "amount": 25.50,
    "description": "Lunch at a restaurant"
  }'
```

# Util commands
## To enter the database
```bash
docker exec -it expenses_postgres bash
psql -h localhost -U dios -d expensedb
```
