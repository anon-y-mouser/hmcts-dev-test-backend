# Case Management API
A Spring Boot REST API for creating, retrieving, updating and deleting cases.
This was created very quickly as I only saw the job advert on the day of closing,
if I had longer I would add a production ready database such as PostgreSQL, add more validation
and documentation and check the dependencies for security issues. I would also modularise
the tests and add more comprehensive and organised testing and comments.

## Getting Started
### Prerequisites
- Java 21
- Gradle

## Run the application
From the project root, run:

`
./gradlew bootRun
`

The application runs at: http://localhost:4000

## API Endpoints

Base URL: http://localhost:4000


## Example Requests
### 1. Create a case

POST /cases

Content-Type: application/json

```
{
"title": "Review application",
"description": "Review submitted documents",
"status": "OPEN",
"dueDate": "2026-11-15T17:00:00"
}
```


Expected response: 201 Created

### 2. Get all cases

GET /cases

Expected response: 200 OK

### 3. Get a case by ID

GET /cases/1

Expected response: 200 OK

### 4. Update a case

PUT /cases/1

Content-Type: application/json

```
{
"title": "Updated application",
"description": "Documents reviewed",
"status": "CLOSED",
"dueDate": "2026-11-20T17:00:00"
}
```


Expected response: 200 OK

### 5. Delete a case

DELETE /cases/1

Expected response: 204 No Content

## Validation
- Title is required and cannot be blank.
- Status must be a valid case status (e.g. OPEN or CLOSED, depending on the configured enum).
- Due date and time are required.
- Due date format: yyyy-MM-dd'T'HH:mm:ss.

Invalid requests return 400 Bad Request.

A case that cannot be found returns 404 Not Found.

## Swagger / OpenAPI Documentation

With the application running, access:

Swagger UI: http://localhost:4000/swagger-ui.html
OpenAPI JSON: http://localhost:4000/v3/api-docs
OpenAPI YAML: http://localhost:4000/v3/api-docs.yaml

To download the OpenAPI specification:

```
Invoke-WebRequest -Uri http://localhost:4000/v3/api-docs.yaml -OutFile swagger.yaml
```

## Run Tests

Run unit tests:

``
./gradlew test
``

Run the full build:

``
./gradlew build
``

## Technology Stack
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- H2 Database
- Gradle
- JUnit 5
- Mockito
- Swagger / OpenAPI
