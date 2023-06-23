Simple User Management RESTful API
This project implements a simple RESTful API in Java using Spring Boot to manage users. It provides endpoints to perform CRUD operations on user entities.

API Endpoints
The following API endpoints are available:
1) GET /api/users: List all users.
2) GET /api/users/{id}: Get a single user by ID.
3) POST /api/users: Create a new user.
4) PUT /api/users: Update an existing user.
5) DELETE /api/users/{id}: Delete a user.

User Entity
The User entity has the following fields:
- id: Unique identifier of the user.
- name: Name of the user.
- email: Email address of the user.
- password: Password of the user.

Build and Run Instructions
To build and run the project, follow these steps:
1) Ensure you have Java 11, Maven, Docker, and Docker Compose installed on your system.
2) Clone the project repository.
3) Navigate to the project root directory.
4) Run command docker compose up -d
   
The API will be available at http://localhost:8080/api/users.

Assumptions and Decisions
* The application uses MongoDB as the database for storing user data. The Docker Compose configuration includes a MongoDB service.
* The User entity has basic fields for id, name, email and password. Additional fields can be added as per requirements.
* Error handling and validation have been implemented to handle invalid requests and provide appropriate responses.
* Testing with JUnit has been implementedt to ensure the correctness of the implemented functionality.
* I decided not to include the user ID in the PUT update API as a path variable, since it is present in the request body.
* Also, I haven't worked with mongo db at all before, so I'm not sure about the best practice of doing the repository level.

Clean Code Principles
In this project, clean code principles such as readability, maintainability, and DRY (Don't Repeat Yourself) have been followed. Some notable practices include:
1) Using meaningful variable and method names to enhance code readability.
2) Breaking down complex logic into smaller, reusable methods for maintainability.
3) Proper Indentation and Formatting.
4) Avoid Magic Numbers and Magic Strings.
