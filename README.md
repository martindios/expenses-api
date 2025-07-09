# Expenses API (using JWT)

# Execute aplication

Once the application is started, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

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
