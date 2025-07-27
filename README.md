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

# Util commands
## To enter the database
```bash
docker exec -it expenses_postgres bash
psql -h localhost -U dios -d expensedb
```
