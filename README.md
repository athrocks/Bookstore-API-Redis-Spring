
## README: Using Postman for API Testing with Redis and Spring Boot

### **Prerequisites**
1. **Postman**: Installed on your machine.
2. **Docker and Docker Compose**: Properly installed and configured.
3. **Redis CLI**: Available in your system for monitoring Redis operations.
4. **Spring Boot Application**: The backend API should be configured to interact with Redis.

---

### **Steps to Start the Setup**

1. **Run the Redis Container**:
   Use Docker Compose to set up and run the Redis container in detached mode:
   ```bash
   docker compose up --build -d
   ```
   This command ensures the Redis server is running and ready to accept connections.

2. **Run the Spring Boot Application**:
   Start your Spring Boot application with Maven:
   ```bash
   mvn clean spring-boot:run
   ```
   This will launch the backend service, typically on `http://localhost:8080`.

3. **Execute below command**
   ```basg
   docker exec -it <container-name or id> bash
   ```
   
4. **Monitor Redis Commands**:
   Use the `redis-cli` to monitor Redis activity in real-time:
   ```bash
   redis-cli monitor
   ```

5. **Open another linux terminal and enter**
    ```bash
   redis-cli
   ```

---

# API Documentation

This document provides an overview of the API endpoints available in this project. The API is built using REST principles and uses JSON for data exchange.

## Base URL

```
http://localhost:8080
```

## Endpoints

### Redis String Operations

#### Set String
- **URL:** `/api/redis/strings`
- **Method:** POST
- **Description:** Set a key-value pair in Redis.
- **Request Body:**
  ```json
  {
    "key": "string",
    "value": "string"
  }
  ```
- **Response:**
  ```json
  {
    "key": "string",
    "value": "string"
  }
  ```
- **Status Code:** `201 Created`

#### Get String
- **URL:** `/api/redis/strings/{key}`
- **Method:** GET
- **Description:** Retrieve the value associated with a key in Redis.
- **Path Parameters:**
  - `key` (string): The Redis key.
- **Response:**
  ```json
  {
    "key": "string",
    "value": "string"
  }
  ```
- **Status Code:** `200 OK`

---

### Cart Operations

#### Get Cart
- **URL:** `/api/carts/{id}`
- **Method:** GET
- **Description:** Retrieve details of a specific cart.
- **Path Parameters:**
  - `id` (string): Cart ID.
- **Response:**
  ```json
  {
    "id": "string",
    "userId": "string",
    "cartItems": [
      {
        "isbn": "string",
        "price": 0.0,
        "quantity": 0
      }
    ],
    "total": 0.0
  }
  ```
- **Status Code:** `200 OK`

#### Add to Cart
- **URL:** `/api/carts/{id}`
- **Method:** POST
- **Description:** Add an item to a cart.
- **Path Parameters:**
  - `id` (string): Cart ID.
- **Request Body:**
  ```json
  {
    "isbn": "string",
    "price": 0.0,
    "quantity": 0
  }
  ```
- **Status Code:** `200 OK`

#### Remove from Cart
- **URL:** `/api/carts/{id}`
- **Method:** DELETE
- **Description:** Remove an item from a cart.
- **Path Parameters:**
  - `id` (string): Cart ID.
- **Request Body:**
  ```json
  "string"
  ```
- **Status Code:** `200 OK`

#### Checkout Cart
- **URL:** `/api/carts/{id}/checkout`
- **Method:** POST
- **Description:** Checkout a cart.
- **Path Parameters:**
  - `id` (string): Cart ID.
- **Status Code:** `200 OK`

---

### User Operations

#### Get Users
- **URL:** `/api/users`
- **Method:** GET
- **Description:** Retrieve a list of users or filter by email.
- **Query Parameters:**
  - `email` (string, optional): Filter by email.
- **Response:**
  ```json
  {
    "users": []
  }
  ```
- **Status Code:** `200 OK`

---

### Book Operations

#### Get All Books
- **URL:** `/api/books`
- **Method:** GET
- **Description:** Retrieve all books.
- **Response:**
  ```json
  {
    "books": []
  }
  ```
- **Status Code:** `200 OK`

#### Get Book by ISBN
- **URL:** `/api/books/{isbn}`
- **Method:** GET
- **Description:** Retrieve details of a book by its ISBN.
- **Path Parameters:**
  - `isbn` (string): Book ISBN.
- **Response:**
  ```json
  {
    "id": "string",
    "title": "string",
    "subtitle": "string",
    "description": "string",
    "language": "string",
    "pageCount": 0,
    "thumbnail": "string",
    "price": 0.0,
    "currency": "string",
    "infoLink": "string",
    "authors": ["string"],
    "categories": [
      {
        "id": "string",
        "name": "string"
      }
    ]
  }
  ```
- **Status Code:** `200 OK`

#### Search Books
- **URL:** `/api/books/search`
- **Method:** GET
- **Description:** Search books by query.
- **Query Parameters:**
  - `q` (string): Search query.
- **Response:**
  ```json
  [
    {
      "id": "string",
      "score": 0.0,
      "sortKey": "string",
      "payload": "string",
      "empty": false
    }
  ]
  ```
- **Status Code:** `200 OK`

#### Get Book Categories
- **URL:** `/api/books/categories`
- **Method:** GET
- **Description:** Retrieve all book categories.
- **Response:**
  ```json
  {
    "categories": []
  }
  ```
- **Status Code:** `200 OK`

#### Author Autocomplete
- **URL:** `/api/books/authors`
- **Method:** GET
- **Description:** Retrieve author suggestions for a search query.
- **Query Parameters:**
  - `q` (string): Search query.
- **Response:**
  ```json
  [
    {
      "string": "string",
      "score": 0.0,
      "payload": "string"
    }
  ]
  ```
- **Status Code:** `200 OK`

---

## Components

### Schemas

#### CartItem
```json
{
  "isbn": "string",
  "price": 0.0,
  "quantity": 0
}
```

#### Cart
```json
{
  "id": "string",
  "userId": "string",
  "cartItems": [
    {
      "isbn": "string",
      "price": 0.0,
      "quantity": 0
    }
  ],
  "total": 0.0
}
```

#### Book
```json
{
  "id": "string",
  "title": "string",
  "subtitle": "string",
  "description": "string",
  "language": "string",
  "pageCount": 0,
  "thumbnail": "string",
  "price": 0.0,
  "currency": "string",
  "infoLink": "string",
  "authors": ["string"],
  "categories": [
    {
      "id": "string",
      "name": "string"
    }
  ]
}


---

### **Error Handling**

1. **Redis Container Issues**:
   - Ensure the container is running:
     ```bash
     docker ps
     ```
   - Restart the container if necessary:
     ```bash
     docker compose up --build -d
     ```

2. **Spring Boot Issues**:
   - Check the logs for any errors during startup.
   - Ensure Redis is properly connected and configured in the Spring Boot application.

3. **Postman Errors**:
   - `Could not get any response`: Check if the Spring Boot application is running and accessible on `http://localhost:8080`.
   - `404 Not Found`: Ensure the endpoint path and method are correct.

---

### **Conclusion**

You are now ready to interact with your Redis-backed Spring Boot application using Postman. Monitor Redis operations in real-time using `redis-cli` to verify the actions performed by the API.
