# Customer API Documentation

## Base URL
```
http://localhost:8080/api/customers
```

## API Overview

This REST API provides complete CRUD operations for managing customers, including search, filter, pagination, and partial update capabilities.

---

## Endpoints

### 1. Get All Customers (with Pagination & Sorting)

**GET** `/api/customers`

Retrieve all customers with optional pagination and sorting.

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Page number (0-based) |
| size | int | 10 | Number of items per page |
| sortBy | string | null | Field to sort by (id, fullName, email, customerCode, createdAt) |
| sortDir | string | asc | Sort direction (asc or desc) |

**Response:** `200 OK`
```json
{
    "customers": [
        {
            "id": 1,
            "customerCode": "C001",
            "fullName": "John Doe",
            "email": "john.doe@example.com",
            "phone": "+1234567890",
            "address": "123 Main St, City, Country",
            "status": "ACTIVE",
            "createdAt": "2024-11-03T10:00:00"
        }
    ],
    "currentPage": 0,
    "totalItems": 1,
    "totalPages": 1
}
```

---

### 2. Get Customer by ID

**GET** `/api/customers/{id}`

Retrieve a specific customer by their unique ID.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Customer ID |

**Response:** `200 OK`
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "address": "123 Main St, City, Country",
    "status": "ACTIVE",
    "createdAt": "2024-11-03T10:00:00"
}
```

---

### 3. Create Customer

**POST** `/api/customers`

Create a new customer.

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "address": "123 Main St, City, Country"
}
```

**Validation Rules:**
| Field | Rules |
|-------|-------|
| customerCode | Required, 3-20 characters, must match pattern `^C\d{3,}$` |
| fullName | Required, 2-100 characters |
| email | Required, valid email format |
| phone | Optional, 10-20 digits, may start with + |
| address | Optional, max 500 characters |

**Response:** `201 Created`
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "address": "123 Main St, City, Country",
    "status": "ACTIVE",
    "createdAt": "2024-11-03T10:00:00"
}
```

---

### 4. Update Customer (Full Update)

**PUT** `/api/customers/{id}`

Update all fields of an existing customer.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Customer ID |

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
    "customerCode": "C001",
    "fullName": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "+1234567899",
    "address": "456 New St, City, Country"
}
```

**Response:** `200 OK`
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "+1234567899",
    "address": "456 New St, City, Country",
    "status": "ACTIVE",
    "createdAt": "2024-11-03T10:00:00"
}
```

---

### 5. Partial Update Customer (PATCH)

**PATCH** `/api/customers/{id}`

Partially update a customer - only provided fields will be updated.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Customer ID |

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:** (all fields optional)
```json
{
    "fullName": "John Doe Patched",
    "phone": "+9876543210"
}
```

**Response:** `200 OK`
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe Patched",
    "email": "john.doe@example.com",
    "phone": "+9876543210",
    "address": "123 Main St",
    "status": "ACTIVE",
    "createdAt": "2024-11-03T10:00:00"
}
```

---

### 6. Delete Customer

**DELETE** `/api/customers/{id}`

Delete a customer by ID.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Customer ID |

**Response:** `200 OK`
```json
{
    "message": "Customer deleted successfully"
}
```

---

### 7. Search Customers

**GET** `/api/customers/search`

Search customers by keyword in name, email, or customer code.

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search term (case-insensitive) |

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1234567890",
        "address": "123 Main St",
        "status": "ACTIVE",
        "createdAt": "2024-11-03T10:00:00"
    }
]
```

---

### 8. Filter by Status

**GET** `/api/customers/status/{status}`

Filter customers by their status.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| status | string | Customer status (ACTIVE or INACTIVE) |

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1234567890",
        "address": "123 Main St",
        "status": "ACTIVE",
        "createdAt": "2024-11-03T10:00:00"
    }
]
```

---

### 9. Advanced Search

**GET** `/api/customers/advanced-search`

Search customers with multiple optional filters.

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| name | string | No | Filter by name (partial match) |
| email | string | No | Filter by email (partial match) |
| status | string | No | Filter by status (ACTIVE/INACTIVE) |

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1234567890",
        "address": "123 Main St",
        "status": "ACTIVE",
        "createdAt": "2024-11-03T10:00:00"
    }
]
```

---

### 10. Get Customers Sorted

**GET** `/api/customers/sorted`

Get all customers with sorting (without pagination).

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| sortBy | string | id | Field to sort by |
| sortDir | string | asc | Sort direction (asc or desc) |

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1234567890",
        "address": "123 Main St",
        "status": "ACTIVE",
        "createdAt": "2024-11-03T10:00:00"
    }
]
```

---

## Error Responses

### 200 OK
Successful request.
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "address": "123 Main St",
    "status": "ACTIVE",
    "createdAt": "2024-11-03T10:00:00"
}
```

### 201 Created
Resource successfully created.
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "address": "123 Main St",
    "status": "ACTIVE",
    "createdAt": "2024-11-03T10:00:00"
}
```

### 400 Bad Request (Validation Error)
Request validation failed.
```json
{
    "timestamp": "2024-11-03T10:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "errors": {
        "email": "Invalid email format",
        "customerCode": "Customer code must start with C followed by numbers",
        "fullName": "Full name is required"
    },
    "path": "/api/customers"
}
```

### 404 Not Found
Requested resource does not exist.
```json
{
    "timestamp": "2024-11-03T10:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Customer not found with id: 999",
    "path": "/api/customers/999"
}
```

### 409 Conflict (Duplicate Resource)
Resource already exists (duplicate customer code or email).
```json
{
    "timestamp": "2024-11-03T10:00:00",
    "status": 409,
    "error": "Conflict",
    "message": "Customer code already exists: C001",
    "path": "/api/customers"
}
```

### 500 Internal Server Error
Unexpected server error.
```json
{
    "timestamp": "2024-11-03T10:00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "An unexpected error occurred. Please try again later.",
    "path": "/api/customers"
}
```

---

## Data Models

### Customer Response DTO
```json
{
    "id": "Long - Unique identifier",
    "customerCode": "String - Unique customer code (e.g., C001)",
    "fullName": "String - Customer's full name",
    "email": "String - Customer's email address",
    "phone": "String - Customer's phone number",
    "address": "String - Customer's address",
    "status": "String - Customer status (ACTIVE/INACTIVE)",
    "createdAt": "LocalDateTime - Creation timestamp"
}
```

### Customer Request DTO (for POST/PUT)
```json
{
    "customerCode": "String - Required, pattern: C followed by 3+ digits",
    "fullName": "String - Required, 2-100 characters",
    "email": "String - Required, valid email format",
    "phone": "String - Optional, 10-20 digits",
    "address": "String - Optional, max 500 characters"
}
```

### Customer Update DTO (for PATCH)
```json
{
    "fullName": "String - Optional, 2-100 characters",
    "email": "String - Optional, valid email format",
    "phone": "String - Optional, 10-20 digits",
    "address": "String - Optional, max 500 characters",
    "status": "String - Optional, ACTIVE or INACTIVE"
}
```

---

## Status Codes Summary

| Code | Description | Use Case |
|------|-------------|----------|
| 200 | OK | Successful GET, PUT, PATCH, DELETE |
| 201 | Created | Successful POST |
| 400 | Bad Request | Validation errors |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 500 | Internal Server Error | Unexpected server error |

---

## Example Usage

### Create a Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "address": "123 Main St"
  }'
```

### Get Paginated Customers
```bash
curl "http://localhost:8080/api/customers?page=0&size=10&sortBy=fullName&sortDir=asc"
```

### Search Customers
```bash
curl "http://localhost:8080/api/customers/search?keyword=john"
```

### Partial Update
```bash
curl -X PATCH http://localhost:8080/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "+9876543210"
  }'
```
