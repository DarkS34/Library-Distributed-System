# Distributed Systems Project - Part 3

This repository fulfills the following functionalities:

## Entities with Semantic Relationship

- Three entities with semantic relationships established among them. These relationships are now implemented within each entity's data model.
- **MySQL Database:** The repository now utilizes MySQL as a relational database for persistence and data storage. Data persistence is ensured by utilizing a Docker image to store the database data.
- **Dockerized:** The application is now dockerized, allowing for easier deployment and management of the system.

## CRUD Operations

- **Forms:** CRUD operations are implemented for each entity through interactive forms. Additionally, the Patch operation is available through forms.
- **REST API:** The repository provides a REST API for performing CRUD operations on each entity. Patch operation is also supported.

## Other

- **Postman Collection:** A Postman collection is included within the repository featuring CRUD + Patch operations for each entity.
- **JavaScript and CSS Usage:** JavaScript and CSS have been utilized to enhance the user experience and interface design.

## Request Body Requirement

For all PUT, POST, and PATCH endpoints, it is mandatory to include the object content in the HTTP request body.

## Running the Application

To run the application, simply execute the following command from the root of the project:

```bash
docker-compose up --build
