# LAB 08 HOMEWORK REPORT - Customer REST API

## Student Information
- **Name:** Võ Trí Khôi
- **Student ID:** ITCSIU24045
- **Class:** Group 2

## API Endpoints

### Base URL
`http://localhost:8080/api/customers`

### Endpoints Implemented
- ✅ GET /api/customers - Get all customers
- ✅ GET /api/customers/{id} - Get by ID
- ✅ POST /api/customers - Create customer
- ✅ PUT /api/customers/{id} - Update customer
- ✅ DELETE /api/customers/{id} - Delete customer
- ✅ GET /api/customers/search?keyword={keyword} - Search
- ✅ GET /api/customers/status/{status} - Filter by status
- ✅ Pagination and sorting
- ✅ PATCH for partial update
- [ ] Bonus features

## How to Run
1. Create database: `customer_management`
2. Update `application.properties` with your MySQL credentials
3. Run: `mvn spring-boot:run`
4. Test: Open Thunder Client or Postman
5. Import collection: `Customer_API.postman_collection.json`

## Testing
All endpoints tested with Thunder Client.
See `screenshots/` folder for test results.

## Features Implemented
- DTO pattern for request/response
- Validation with @Valid
- Exception handling with @RestControllerAdvice
- Custom exceptions (404, 409)
- Proper HTTP status codes
- Search and filter
- Pagination
- Sorting

## Known Issues
- [List any bugs]

## Time Spent
Approximately [X] hours

## Project Structure
```
customer-api.zip
├── src/main/java/com/example/customerapi/
│   ├── entity/Customer.java
│   ├── dto/
│   │   ├── CustomerRequestDTO.java
│   │   ├── CustomerResponseDTO.java
│   │   └── ErrorResponseDTO.java
│   ├── repository/CustomerRepository.java
│   ├── service/
│   │   ├── CustomerService.java
│   │   └── CustomerServiceImpl.java
│   ├── controller/CustomerRestController.java
│   └── exception/
│       ├── ResourceNotFoundException.java
│       ├── DuplicateResourceException.java
│       └── GlobalExceptionHandler.java
├── pom.xml
└── README.md
```

## Application Code Flow

### 1. GET All Customers Flow
This flow retrieves the complete list of all customers in the system.

1. **HTTP Request**: Client sends `GET http://localhost:8080/api/customers`.
2. **Controller**: `CustomerRestController.getAllCustomers()` receives the request.
3. **Service Layer**: Controller calls `CustomerService.getAllCustomers()`.
4. **Repository**: Service calls `CustomerRepository.findAll()` to fetch all customer records from the database.
5. **DTO Conversion**: Each `Customer` entity is converted to `CustomerResponseDTO` using `convertToResponseDTO()`.
6. **Response**: Returns `200 OK` with a JSON array of customer objects.

**Testing:**
```
Method: GET
URL: http://localhost:8080/api/customers

Expected Response (200 OK):
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1-555-0101",
        "address": "123 Main St, New York, NY 10001",
        "status": "ACTIVE",
        "createdAt": "2024-11-03T10:00:00"
    },
    ...
]
```

<img width="508" height="614" alt="image" src="https://github.com/user-attachments/assets/fcc430d1-a992-4e8b-a70a-07d3f52e2003" />

<img width="508" height="521" alt="image" src="https://github.com/user-attachments/assets/3416a790-ce23-4ee9-9dd2-2538561efd0b" />

### 2. GET Customer by ID Flow
This flow retrieves a single customer by their unique identifier.

1. **HTTP Request**: Client sends `GET http://localhost:8080/api/customers/{id}`.
2. **Controller**: `CustomerRestController.getCustomerById(Long id)` extracts the ID from the URL path.
3. **Service Layer**: Controller calls `CustomerService.getCustomerById(id)`.
4. **Repository**: Service calls `CustomerRepository.findById(id)`.
5. **Exception Handling**: 
   * **Success**: If found, the entity is converted to `CustomerResponseDTO`.
   * **Failure**: If not found, throws `ResourceNotFoundException` (handled by Global Exception Handler).
6. **Response**: Returns `200 OK` with the customer object, or `404 Not Found` if the customer doesn't exist.

**Testing:**
```
Method: GET
URL: http://localhost:8080/api/customers/1

Expected Response (200 OK):
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    ...
}
```

<img width="508" height="300" alt="image" src="https://github.com/user-attachments/assets/66c230ca-9507-4df4-90e1-e7e4e556e205" />

### 3. POST Create Customer Flow
This flow creates a new customer in the system with validation.

1. **HTTP Request**: Client sends `POST http://localhost:8080/api/customers` with JSON body.
2. **Validation**: Spring's `@Valid` annotation triggers validation on `CustomerRequestDTO`:
   * Customer code format: Must start with 'C' followed by numbers (e.g., C001).
   * Email format validation.
   * Phone number: Must be 10-20 digits.
   * All required fields must be present.
3. **Controller**: `CustomerRestController.createCustomer(@Valid CustomerRequestDTO)` receives validated data.
4. **Service Layer**: Controller calls `CustomerService.createCustomer(requestDTO)`.
5. **Duplicate Check**: Service validates:
   * `CustomerRepository.existsByCustomerCode()` - Checks if customer code already exists.
   * `CustomerRepository.existsByEmail()` - Checks if email already exists.
   * If duplicates found, throws `DuplicateResourceException`.
6. **Entity Conversion**: Converts `CustomerRequestDTO` to `Customer` entity using `convertToEntity()`.
7. **Database Save**: `CustomerRepository.save(customer)` persists the new customer.
8. **Response Conversion**: Saved entity is converted to `CustomerResponseDTO`.
9. **Response**: Returns `201 Created` with the newly created customer object.

**Testing:**
```
Method: POST
URL: http://localhost:8080/api/customers
Headers: Content-Type: application/json

Body (JSON):
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "+1555010600",
    "address": "999 Broadway, Seattle, WA 98101"
}

Expected Response (201 Created):
{
    "id": 6,
    "customerCode": "C006",
    "fullName": "David Miller",
    ...
}
```

<img width="508" height="294" alt="image" src="https://github.com/user-attachments/assets/81c13fca-64a6-4ced-9495-c94e4e23c07c" />

### 4. PUT Update Customer Flow
This flow updates an existing customer's information.

1. **HTTP Request**: Client sends `PUT http://localhost:8080/api/customers/{id}` with JSON body.
2. **Validation**: `@Valid` annotation validates the `CustomerRequestDTO` input.
3. **Controller**: `CustomerRestController.updateCustomer(Long id, @Valid CustomerRequestDTO)` receives the request.
4. **Service Layer**: Controller calls `CustomerService.updateCustomer(id, requestDTO)`.
5. **Existence Check**: Service calls `CustomerRepository.findById(id)`:
   * **Not Found**: Throws `ResourceNotFoundException`.
   * **Found**: Retrieves existing customer entity.
6. **Email Duplicate Check**: If email is being changed, validates that the new email doesn't already exist:
   * Calls `CustomerRepository.existsByEmail()`.
   * If duplicate found, throws `DuplicateResourceException`.
7. **Update Fields**: Updates mutable fields (fullName, email, phone, address). Note: `customerCode` is immutable.
8. **Database Save**: `CustomerRepository.save(existingCustomer)` persists the changes.
9. **Response Conversion**: Updated entity is converted to `CustomerResponseDTO`.
10. **Response**: Returns `200 OK` with the updated customer object.

**Testing:**
```
Method: PUT
URL: http://localhost:8080/api/customers/6
Headers: Content-Type: application/json

Body (JSON):
{
    "customerCode": "C006",
    "fullName": "David Miller Jr.",
    "email": "david.miller.jr@example.com",
    "phone": "+15550107",
    "address": "1000 Broadway, Seattle, WA 98101"
}

Expected Response (200 OK):
{
    "id": 8,
    "customerCode": "C006",
    "fullName": "David Miller Jr.",
    ...
}
```

<img width="509" height="304" alt="image" src="https://github.com/user-attachments/assets/132a8729-9889-4432-9b9a-25c987843e0d" />

### 5. DELETE Customer Flow
This flow removes a customer from the system.

1. **HTTP Request**: Client sends `DELETE http://localhost:8080/api/customers/{id}`.
2. **Controller**: `CustomerRestController.deleteCustomer(Long id)` extracts the ID.
3. **Service Layer**: Controller calls `CustomerService.deleteCustomer(id)`.
4. **Existence Check**: Service calls `CustomerRepository.existsById(id)`:
   * **Not Found**: Throws `ResourceNotFoundException`.
   * **Found**: Proceeds with deletion.
5. **Database Delete**: `CustomerRepository.deleteById(id)` removes the customer.
6. **Response**: Returns `200 OK` with a success message.

**Testing:**
```
Method: DELETE
URL: http://localhost:8080/api/customers/8

Expected Response (200 OK):
{
    "message": "Customer deleted successfully"
}
```

<img width="508" height="179" alt="image" src="https://github.com/user-attachments/assets/ebfcfe2b-46ed-43d3-9ca9-16216dcb5969" />

### 6. Search Customers Flow
This flow searches for customers by keyword across multiple fields.

1. **HTTP Request**: Client sends `GET http://localhost:8080/api/customers/search?keyword={keyword}`.
2. **Controller**: `CustomerRestController.searchCustomers(@RequestParam String keyword)` extracts the query parameter.
3. **Service Layer**: Controller calls `CustomerService.searchCustomers(keyword)`.
4. **Repository**: Service calls `CustomerRepository.searchCustomers(keyword)`:
   * Uses custom JPQL query to search across `fullName`, `email`, and `customerCode` fields.
   * Performs case-insensitive partial matching using `LIKE '%keyword%'`.
5. **DTO Conversion**: Each matching `Customer` entity is converted to `CustomerResponseDTO`.
6. **Response**: Returns `200 OK` with an array of matching customers (empty array if no matches).

**Example Request:** `GET /api/customers/search?keyword=john`

**Testing:**
```
Method: GET
URL: http://localhost:8080/api/customers/search?keyword=john

Expected Response (200 OK):
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        ...
    },
    {
        "id": 3,
        "customerCode": "C003",
        "fullName": "Bob Johnson",
        ...
    }
]
```

<img width="509" height="534" alt="image" src="https://github.com/user-attachments/assets/643ef9d0-a79a-437b-a401-042d86c9578a" />

### 7. Validation Error Flow (400 Bad Request)
This flow handles invalid input data during CREATE or UPDATE operations.

1. **Trigger**: Client sends invalid data (e.g., missing required fields, invalid format).
2. **Validation**: Spring's `@Valid` annotation triggers validation on the DTO:
   * Validates `@NotBlank`, `@Email`, `@Pattern`, `@Size` constraints.
   * If validation fails, throws `MethodArgumentNotValidException`.
3. **Exception Handler**: `GlobalExceptionHandler.handleValidationException()` catches the exception:
   * Extracts all validation errors from `BindingResult`.
   * Creates `ErrorResponseDTO` with status `400` and detailed error messages.
4. **Response**: Returns `400 Bad Request` with structured error information.

**Testing:**
```
Method: POST
URL: http://localhost:8080/api/customers
Headers: Content-Type: application/json

Body (Invalid - missing required fields):
{
    "customerCode": "C",
    "email": "invalid-email"
}

Expected Response (400 Bad Request):
{
    "timestamp": "2024-11-03T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid input data",
    "path": "/api/customers",
    "details": [
        "customerCode: Customer code must be 3-20 characters",
        "fullName: Full name is required",
        "email: Invalid email format"
    ]
}
```

<img width="508" height="399" alt="image" src="https://github.com/user-attachments/assets/95c2e5f2-92fc-494a-b7d1-e3ed978c0401" />

### 8. Resource Not Found Flow (404 Not Found)
This flow handles requests for non-existent resources.

1. **Trigger**: Client requests a customer with an ID that doesn't exist (GET, PUT, or DELETE).
2. **Repository Check**: `CustomerRepository.findById()` or `existsById()` returns empty.
3. **Exception Thrown**: Service layer throws `ResourceNotFoundException` with descriptive message.
4. **Exception Handler**: `GlobalExceptionHandler.handleResourceNotFoundException()` catches the exception:
   * Creates `ErrorResponseDTO` with status `404`.
5. **Response**: Returns `404 Not Found` with error details.

**Example Request:** `GET /api/customers/999` (ID doesn't exist)

**Testing:**
```
Method: GET
URL: http://localhost:8080/api/customers/999

Expected Response (404 Not Found):
{
    "timestamp": "2024-11-03T10:35:00",
    "status": 404,
    "error": "Not Found",
    "message": "Customer not found with id: 999",
    "path": "/api/customers/999"
}
```

<img width="508" height="269" alt="image" src="https://github.com/user-attachments/assets/7bca129f-c2c1-41bc-8534-089f767f4fe1" />

### 9. Duplicate Resource Flow (409 Conflict)
This flow handles attempts to create or update with duplicate unique fields.

1. **Trigger**: Client attempts to:
   * Create a customer with existing `customerCode` or `email`.
   * Update a customer's email to one that already exists.
2. **Duplicate Check**: Service layer calls:
   * `CustomerRepository.existsByCustomerCode()` - Returns `true` if code exists.
   * `CustomerRepository.existsByEmail()` - Returns `true` if email exists.
3. **Exception Thrown**: Service throws `DuplicateResourceException` with specific message.
4. **Exception Handler**: `GlobalExceptionHandler.handleDuplicateResourceException()` catches the exception:
   * Creates `ErrorResponseDTO` with status `409`.
5. **Response**: Returns `409 Conflict` with error details.

**Testing:**
```
Method: POST
URL: http://localhost:8080/api/customers
Headers: Content-Type: application/json

Body (Duplicate email):
{
    "customerCode": "C007",
    "fullName": "Test User",
    "email": "john.doe@example.com"
}

Expected Response (409 Conflict):
{
    "timestamp": "2024-11-03T10:40:00",
    "status": 409,
    "error": "Conflict",
    "message": "Email already exists: john.doe@example.com",
    "path": "/api/customers"
}

```

<img width="508" height="270" alt="image" src="https://github.com/user-attachments/assets/26eaa384-bde6-408f-9df9-aa32a4b374f7" />

### 10. Filter Customers by Status Flow
This flow retrieves customers filtered by their status (ACTIVE or INACTIVE).

1. **HTTP Request**: Client sends `GET http://localhost:8080/api/customers/status/{status}`.
2. **Controller**: `CustomerRestController.getCustomersByStatus(@PathVariable String status)` extracts the status from the URL path.
3. **Service Layer**: Controller calls `CustomerService.getCustomersByStatus(status)`.
4. **Status Validation**: Service converts the string status to `CustomerStatus` enum:
   * **Valid**: Proceeds with the query.
   * **Invalid**: Throws `IllegalArgumentException` with message about valid values.
5. **Repository**: Service calls `CustomerRepository.findByStatus(CustomerStatus status)` to fetch matching customers.
6. **DTO Conversion**: Each matching `Customer` entity is converted to `CustomerResponseDTO`.
7. **Response**: Returns `200 OK` with an array of customers matching the status.

**Testing:**

```
GET /api/customers/search?keyword=john
```

<img width="507" height="528" alt="image" src="https://github.com/user-attachments/assets/0ce801aa-d8e1-4706-9225-bc72e3ceabba" />

```
GET /api/customers/status/ACTIVE
```

<img width="508" height="459" alt="image" src="https://github.com/user-attachments/assets/202e4b04-3d5a-45b1-8896-5320730e6c7f" />

<img width="508" height="546" alt="image" src="https://github.com/user-attachments/assets/4a3bb58a-f5f5-4d60-8c0a-47ce5f9a8ced" />

```
GET /api/customers/status/INACTIVE
```

<img width="507" height="343" alt="image" src="https://github.com/user-attachments/assets/02584ef0-9c4d-424e-9b19-35104aa659d0" />

### 11. Pagination and Sorting Flow
This flow retrieves customers with pagination and optional sorting support.

1. **HTTP Request**: Client sends `GET http://localhost:8080/api/customers?page={page}&size={size}&sortBy={field}&sortDir={direction}`.
2. **Controller**: `CustomerRestController.getAllCustomers()` receives the request with optional parameters:
   * `page` (default: 0) - Zero-based page index.
   * `size` (default: 10) - Number of items per page.
   * `sortBy` (optional) - Field to sort by (e.g., fullName, email, createdAt).
   * `sortDir` (default: asc) - Sort direction (asc or desc).
3. **Service Layer**: Controller calls appropriate service method:
   * **With sorting**: `CustomerService.getAllCustomers(page, size, sortBy, sortDir)`.
   * **Without sorting**: `CustomerService.getAllCustomers(page, size)`.
4. **Pageable Creation**: Service creates a `Pageable` object:
   * Uses `PageRequest.of(page, size)` for pagination only.
   * Uses `PageRequest.of(page, size, Sort)` when sorting is requested.
5. **Repository**: Service calls `CustomerRepository.findAll(Pageable pageable)` to fetch paginated results.
6. **Page Processing**: The `Page<Customer>` result is mapped to `Page<CustomerResponseDTO>`.
7. **Response Building**: Controller builds response map containing:
   * `customers` - List of customer DTOs for current page.
   * `currentPage` - Current page number.
   * `totalItems` - Total number of customers.
   * `totalPages` - Total number of pages.
8. **Response**: Returns `200 OK` with paginated response object.

**Testing:**

```
GET /api/customers?page=0&size=5
```

<img width="508" height="606" alt="image" src="https://github.com/user-attachments/assets/7191fd3a-2e97-4345-b4ce-80d1a180d50d" />

<img width="509" height="518" alt="image" src="https://github.com/user-attachments/assets/757871cc-32ac-4387-9c1d-5d9f6dea0abb" />

<img width="508" height="68" alt="image" src="https://github.com/user-attachments/assets/e2f33a46-3b6e-41a2-8389-a92a51f2c24d" />

```
GET /api/customers?page=1&size=10
```

<img width="508" height="224" alt="image" src="https://github.com/user-attachments/assets/2492c047-0098-4285-9a48-47c9f5ed8d07" />

```
GET /api/customers?sortBy=fullName&sortDir=asc
```

<img width="508" height="608" alt="image" src="https://github.com/user-attachments/assets/d9f6be5f-7c6c-480a-a665-215fc7a1cc65" />

<img width="508" height="474" alt="image" src="https://github.com/user-attachments/assets/945c7565-b248-47c4-9226-161ed6ceef1e" />

<img width="509" height="110" alt="image" src="https://github.com/user-attachments/assets/18bdc69a-11be-4633-aaca-93637c81776b" />

```
GET /api/customers?sortBy=createdAt&sortDir=desc
```

<img width="508" height="607" alt="image" src="https://github.com/user-attachments/assets/f772e2c8-427f-4472-aee8-68f15dca5bae" />

<img width="509" height="478" alt="image" src="https://github.com/user-attachments/assets/ed89d17f-0170-4149-b294-c90c5430c876" />

<img width="510" height="112" alt="image" src="https://github.com/user-attachments/assets/e6a405fb-ba2d-4d5a-b96f-8dde5678bf86" />

```
GET /api/customers?page=0&size=5&sortBy=fullName&sortDir=asc
```

<img width="508" height="613" alt="image" src="https://github.com/user-attachments/assets/121dfe72-13cf-4122-b17b-c746c611e476" />

<img width="508" height="476" alt="image" src="https://github.com/user-attachments/assets/042e581a-0a4f-45c2-b570-3cda5cfde4d4" />

<img width="508" height="117" alt="image" src="https://github.com/user-attachments/assets/2afc100b-a81b-4945-a591-62f24894350c" />

### 12. PATCH Partial Update Customer Flow
This flow partially updates a customer's information, only modifying the fields provided in the request.

1. **HTTP Request**: Client sends `PATCH http://localhost:8080/api/customers/{id}` with JSON body containing only fields to update.
2. **Validation**: Spring's `@Valid` annotation triggers validation on `CustomerUpdateDTO`:
   * All fields are optional (no `@NotBlank` constraints).
   * Only provided fields are validated (e.g., email format if email is included).
3. **Controller**: `CustomerRestController.partialUpdateCustomer(Long id, @Valid CustomerUpdateDTO)` receives the request.
4. **Service Layer**: Controller calls `CustomerService.partialUpdateCustomer(id, updateDTO)`.
5. **Existence Check**: Service calls `CustomerRepository.findById(id)`:
   * **Not Found**: Throws `ResourceNotFoundException`.
   * **Found**: Retrieves existing customer entity.
6. **Selective Field Update**: Service checks each field in `CustomerUpdateDTO`:
   * **fullName**: If not null, updates `customer.setFullName()`.
   * **email**: If not null, validates uniqueness and updates `customer.setEmail()`.
   * **phone**: If not null, updates `customer.setPhone()`.
   * **address**: If not null, updates `customer.setAddress()`.
   * **status**: If not null, converts to enum and updates `customer.setStatus()`.
7. **Email Duplicate Check**: If email is being changed:
   * Calls `CustomerRepository.existsByEmail()`.
   * If duplicate found, throws `DuplicateResourceException`.
8. **Database Save**: `CustomerRepository.save(customer)` persists the partial changes.
9. **Response Conversion**: Updated entity is converted to `CustomerResponseDTO`.
10. **Response**: Returns `200 OK` with the updated customer object.

**Key Difference from PUT:**
* **PUT** requires all fields and replaces the entire resource.
* **PATCH** only updates the fields provided in the request body.

**Testing:**

```
PUT /api/customers/1
{
    "customerCode": "C001",
    "fullName": "John Updated",
    "email": "john.updated@example.com",
    "phone": "+1-555-9999",
    "address": "New Address"
}
```

<img width="507" height="311" alt="image" src="https://github.com/user-attachments/assets/e7b14b3c-52bc-4d01-8993-ea1f37480723" />

```
PATCH /api/customers/1
{
    "fullName": "John Partially Updated"
}
```

<img width="508" height="313" alt="image" src="https://github.com/user-attachments/assets/ebcf4349-8f40-4f47-93df-d1ee52f6ba20" />

### 13. Advanced Search Flow
This flow searches for customers using multiple optional criteria simultaneously.

1. **HTTP Request**: Client sends `GET http://localhost:8080/api/customers/advanced-search?name={name}&email={email}&status={status}`.
2. **Controller**: `CustomerRestController.advancedSearch()` extracts optional query parameters:
   * `name` (optional) - Partial match on customer name.
   * `email` (optional) - Partial match on email.
   * `status` (optional) - Exact match on status (ACTIVE/INACTIVE).
3. **Service Layer**: Controller calls `CustomerService.advancedSearch(name, email, status)`.
4. **Status Conversion**: If status is provided, service converts string to `CustomerStatus` enum:
   * **Valid**: Proceeds with enum value.
   * **Invalid**: Throws `IllegalArgumentException`.
5. **Repository**: Service calls `CustomerRepository.advancedSearch(name, email, customerStatus)`:
   * Uses custom JPQL query with conditional WHERE clauses.
   * Each parameter is only applied if not null.
   * Performs case-insensitive partial matching for name and email.
6. **DTO Conversion**: Each matching `Customer` entity is converted to `CustomerResponseDTO`.
7. **Response**: Returns `200 OK` with an array of matching customers.

## Architecture Components

### Controller Layer (`CustomerRestController`)
* Handles HTTP requests and responses.
* Performs request/response mapping using `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`.
* Validates input using `@Valid` annotation.
* Returns appropriate HTTP status codes.

### Service Layer (`CustomerService` & `CustomerServiceImpl`)
* Contains business logic and validation rules.
* Performs duplicate checks before CREATE/UPDATE operations.
* Handles DTO to Entity conversion and vice versa.
* Throws custom exceptions for error scenarios.

### Repository Layer (`CustomerRepository`)
* Extends `JpaRepository` for database operations.
* Provides custom query methods (`findByStatus`, `existsByEmail`, etc.).
* Uses JPQL for complex search queries.

### Exception Handling (`GlobalExceptionHandler`)
* Centralized error handling using `@RestControllerAdvice`.
* Catches and transforms exceptions into standardized error responses.
* Handles:
  * `ResourceNotFoundException` → 404
  * `DuplicateResourceException` → 409
  * `MethodArgumentNotValidException` → 400
  * Generic `Exception` → 500

### DTO Layer
* **`CustomerRequestDTO`**: Input validation and data transfer for CREATE/UPDATE.
* **`CustomerResponseDTO`**: Output data structure for API responses.
* **`ErrorResponseDTO`**: Standardized error response format.

### Entity Layer
* **`Customer`**: JPA entity mapping to the `customers` table.
* **`CustomerStatus`**: Enum defining customer statuses (ACTIVE, INACTIVE).
