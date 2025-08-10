# Event Booking System

## Introduction

A microservices-based application for booking events. This project provides user management and service discovery capabilities.

## Architecture

The application follows a microservices architecture, with different services responsible for specific functionalities. A discovery service (Eureka) is used for service registration and discovery, allowing services to communicate with each other without hardcoding hostnames and ports.

## Services

-   **Discovery Service (`discovery-service`)**:
    -   Acts as a service registry for all other microservices.
    -   Uses Spring Cloud Netflix Eureka.
    -   All other services register themselves with this service.
-   **User Service (`user-service`)**:
    -   Manages user-related operations like registration, login, profile updates, and role management.
    -   Uses Spring Boot, Spring Security with JWT for authentication and authorization.
    -   Connects to a MySQL database to store user data.
    -   Registers itself with the discovery service.

## Technologies Used

-   Java 21
-   Spring Boot 3.5
-   Spring Cloud
-   Spring Security
-   JWT (JSON Web Tokens)
-   MySQL
-   Maven
-   Eureka (for Service Discovery)
-   Lombok
-   ModelMapper

## Prerequisites

-   JDK 21 or later
-   Maven 3.2+
-   MySQL 8.0 or later
-   An IDE like IntelliJ IDEA or VS Code

## Getting Started

### Cloning

```bash
git clone https://github.com/Falasefemi2/event-booking.git
```

### Database Setup

1.  Create a MySQL database named `event_usersdb`.
2.  The service will create the tables automatically on startup.

### Configuration

The `user-service` uses a `.env` file for environment variables. Create a `.env` file in the root of the `user-service` directory with the following content:

```
DB_USERNAME=<your-mysql-username>
DB_PASSWORD=<your-mysql-password>
JWT_SECRET=<your-jwt-secret>
```

Alternatively, you can configure the database credentials directly in `user-service/src/main/resources/application.properties`.

### Running the application

1.  **Start the `discovery-service`**: Navigate to the `discovery-service` directory and run `mvn spring-boot:run`. The Eureka server will be accessible at `http://localhost:8761`.
2.  **Start the `user-service`**: Navigate to the `user-service` directory and run `mvn spring-boot:run`.

## API Endpoints (`user-service`)

All endpoints are prefixed with `/api`.

### Authentication

-   `POST /auth/register`: Register a new user.
    -   **Request Body**: `RegisterRequestDTO` (`username`, `email`, `password`)
    -   **Response**: `AuthResponseDTO` (`token`, `user`)
-   `POST /auth/login`: Login an existing user.
    -   **Request Body**: `LoginRequestDTO` (`email`, `password`)
    -   **Response**: `AuthResponseDTO` (`token`, `user`)

### User Management

-   `GET /users/profile`: Get the profile of the currently authenticated user.
    -   **Authorization**: `USER` or `ADMIN` role required.
    -   **Response**: `UserDTO`
-   `PUT /users/profile`: Update the profile of the currently authenticated user.
    -   **Authorization**: `USER` or `ADMIN` role required.
    -   **Request Body**: `UpdateProfileRequestDTO` (`username`, `email`)
    -   **Response**: `UserDTO`
-   `PUT /users/change-password`: Change the password of the currently authenticated user.
    -   **Authorization**: `USER` or `ADMIN` role required.
    -   **Request Body**: `ChangePasswordRequestDTO` (`oldPassword`, `newPassword`)
-   `GET /users`: Get a list of all users.
    -   **Authorization**: `ADMIN` role required.
    -   **Response**: `List<UserDTO>`
-   `GET /users/{id}`: Get a user by their ID.
    -   **Authorization**: `ADMIN` role required.
    -   **Response**: `UserDTO`
-   `DELETE /users/{id}`: Delete a user by their ID.
    -   **Authorization**: `ADMIN` role required.
-   `PUT /users/{id}/role`: Update the role of a user by their ID.
    -   **Authorization**: `ADMIN` role required.
    -   **Request Body**: `UpdateRoleRequestDTO` (`role`)
    -   **Response**: `UserDTO`

## Future Improvements

-   Implement an `event-service` to manage events.
-   Implement a `booking-service` to handle event bookings.
-   Add more comprehensive testing.
-   Implement a gateway service (e.g., using Spring Cloud Gateway) to act as a single entry point for all services.
-   Integrate with a centralized logging and monitoring solution.
