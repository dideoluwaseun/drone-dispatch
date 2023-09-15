# Drone Dispatch Service

This is a secure REST API service built with Maven and Spring Boot
using Java 17. The service manages a fleet of drones capable of carrying
medications. Each drone has specific attributes, and the service provides 
various endpoints to interact with these drones. Users are required to sign up and use 
JWT for authentication. This service uses in-memory H2 database, database configuration properties can be found
in the `application.properties` file of the project.

## Requirements

### Features

- Register a drone with its details.
- Create medications with specific attributes.
- Retrieve a list of all medications with pagination support.
- Retrieve a list of all drones with pagination support.
- Retrieve loaded medication items and battery level for a given drone.
- Load medications onto a drone.
- Get available drones for loading based on battery capacity and state.
- Update the state and battery capacity of a drone.
- Periodic task to check battery levels and log audit.
- Required data has been preloaded in the Database using Flyway Migration.


## Getting Started

To run the Drone Dispatch Service locally, follow these instructions:

1. Clone the repository to your local machine:

    `git clone https://github.com/dideoluwaseun/drone-dispatch.git`



2. Navigate to the project directory:

   `cd drone-dispatch`


3. Build the project using Maven:
   You can build the project using the maven wrapper by running
      `./mvnw clean install`



4. Run the service with the following command:
   `java -jar drone-dispatch-0.0.1-SNAPSHOT.jar`



The service will start on port 8080 by default.

## Endpoints
### User Registration

**Endpoint:** `/api/v1/users/sign-up`
**Method:** `POST`

Registers a new user. Requires providing a `username`, `password` and `userRole` in the request body.
User roles available are ROLE_ADMIN and ROLE_USER.
Returns access Token.

### User Login

**Endpoint:** `/api/v1/users/sign-in`
**Method:** `POST`

Authenticates a user. Requires providing a `username` and `password` as request parameters. Returns a JWT token upon successful authentication.

### Drone Dispatch
Here are the available endpoints of the Drone Dispatch Service:

1. **Register a Drone**: `POST /api/v1/drone-dispatch/drone/register`

Example Request Body:
```json
{
  "serialNumber": "DRONE001",
  "model": "LIGHTWEIGHT",
  "batteryCapacity": 6
}
```
## Security
This service is secured using JWT (JSON Web Token) for user authentication.
Users need to sign up and obtain a JWT token to access the protected endpoints.

## Scheduled Task
The service includes a scheduled task that runs every 15 minutes to check the battery levels of drones and log audit events.

## Database
The service uses an in-memory H2 database with Flyway migration to preload the required data.

## Testing
You can run tests using the following command:
      `./mvnw test`




