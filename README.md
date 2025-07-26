# Library Distributed System

This repository corresponds to the third part of the Distributed Systems project. It integrates and expands upon all functionalities developed in the previous stages.

## Entities with Semantic Relationships

The system manages three entities with semantic relationships between them:

* **Books**
* **Libraries**
* **Students**

These relationships are defined and implemented within each entity's data model.

## Data Persistence

* **MySQL Database**: Unlike previous versions (which used in-memory storage and H2), this version uses MySQL as the relational database.
* **Dockerized Environment**: The application is containerized using Docker, ensuring consistent deployment and persistent storage via Docker volumes.

## CRUD Operations

Each entity supports full CRUD operations through two interfaces:

* **Interactive Forms**: Create, Read, Update, Delete, and Patch via user-friendly forms.
* **REST API**: Complete RESTful API with support for all CRUD and partial update (PATCH) operations.

## HTTP Request Body Requirement

For all `PUT`, `POST`, and `PATCH` requests, the object data must be included in the HTTP request body.

## Running the Application

To start the application, run the following command from the project root:

```bash
docker-compose up --build
```

## API Endpoints

### Books

| HTTP Method | Endpoint               | Description                 |
| ----------- | ---------------------- | --------------------------- |
| GET         | /api/books             | Retrieve all books          |
| GET         | /api/books/{id}        | Retrieve book by ID         |
| POST        | /api/books/create-book | Create a new book           |
| PUT         | /api/books/{id}        | Update book by ID           |
| DELETE      | /api/books/{id}        | Delete book by ID           |
| PATCH       | /api/books/{id}        | Partially update book by ID |

### Libraries

| HTTP Method | Endpoint                      | Description                    |
| ----------- | ----------------------------- | ------------------------------ |
| GET         | /api/libraries                | Retrieve all libraries         |
| GET         | /api/libraries/{id}           | Retrieve library by ID         |
| POST        | /api/libraries/create-library | Create a new library           |
| PUT         | /api/libraries/{id}           | Update library by ID           |
| DELETE      | /api/libraries/{id}           | Delete library by ID           |
| PATCH       | /api/libraries/{id}           | Partially update library by ID |

### Students

| HTTP Method | Endpoint                     | Description                    |
| ----------- | ---------------------------- | ------------------------------ |
| GET         | /api/students                | Retrieve all students          |
| GET         | /api/students/{id}           | Retrieve student by ID         |
| POST        | /api/students/create-student | Create a new student           |
| PUT         | /api/students/{id}           | Update student by ID           |
| DELETE      | /api/students/{id}           | Delete student by ID           |
| PATCH       | /api/students/{id}           | Partially update student by ID |

## Additional Resources

* **Postman Collection**: Included in the repository for testing all CRUD and PATCH operations.
* **Frontend Enhancements**: JavaScript and CSS have been used to enhance user experience and interface design.
