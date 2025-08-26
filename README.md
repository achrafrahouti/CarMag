# Renault Garage Management Microservice

A Spring Boot microservice for managing Renault garages, vehicles, and accessories.

## Overview

This microservice provides a comprehensive solution for managing Renault's network of affiliated garages, the vehicles they store, and the accessories associated with these vehicles. It implements a RESTful API with CRUD operations and includes an event-driven architecture for vehicle-related events.

## Features

### Garage Management
- Create, update, and delete garages
- Retrieve a specific garage by ID
- List all garages with pagination and sorting options
- Search garages by name, city, vehicle type, or accessory availability

### Vehicle Management
- Add, update, and delete vehicles associated with a garage
- List vehicles by garage
- List vehicles by model across multiple garages
- Enforce a maximum of 50 vehicles per garage

### Accessory Management
- Add, update, and delete accessories associated with a vehicle
- List accessories by vehicle
- Search accessories by type or price

### Event-Driven Architecture
- Publish events when vehicles are created, updated, or deleted
- Consume vehicle events for further processing

## Technical Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 17
- **Database**: H2 (file-based for persistence)
- **Messaging**: Apache Kafka
- **Documentation**: SpringDoc OpenAPI
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## API Endpoints

### Garage API
- `POST /api/garages` - Create a new garage
- `GET /api/garages/{id}` - Get a garage by ID
- `GET /api/garages` - Get all garages (paginated)
- `PUT /api/garages/{id}` - Update a garage
- `DELETE /api/garages/{id}` - Delete a garage
- `GET /api/garages/search/name` - Search garages by name
- `GET /api/garages/search/city` - Search garages by city
- `GET /api/garages/search/vehicle-brand` - Search garages by vehicle brand
- `GET /api/garages/search/vehicle-model` - Search garages by vehicle model
- `GET /api/garages/search/accessory-type` - Search garages by accessory type

### Vehicle API
- `POST /api/vehicles` - Create a new vehicle
- `GET /api/vehicles/{id}` - Get a vehicle by ID
- `GET /api/vehicles/garage/{garageId}` - Get vehicles by garage ID
- `GET /api/vehicles/model/{model}` - Get vehicles by model
- `PUT /api/vehicles/{id}` - Update a vehicle
- `DELETE /api/vehicles/{id}` - Delete a vehicle

### Accessory API
- `POST /api/accessories` - Create a new accessory
- `GET /api/accessories/{id}` - Get an accessory by ID
- `GET /api/accessories/vehicle/{vehicleId}` - Get accessories by vehicle ID
- `GET /api/accessories/type/{type}` - Get accessories by type
- `PUT /api/accessories/{id}` - Update an accessory
- `DELETE /api/accessories/{id}` - Delete an accessory

## Data Models

### Garage
- `id`: Long
- `name`: String (required)
- `address`: String (required)
- `telephone`: String (required)
- `email`: String (required, valid email)
- `openingHours`: Map<DayOfWeek, String> (required)
- `vehicles`: List<Vehicle>

### Vehicle
- `id`: Long
- `brand`: String (required)
- `model`: String (required)
- `manufacturingYear`: Integer (required, past)
- `fuelType`: String (required)
- `garage`: Garage
- `accessories`: List<Accessory>

### Accessory
- `id`: Long
- `name`: String (required)
- `description`: String (required)
- `price`: BigDecimal (required, positive)
- `type`: String (required)
- `vehicle`: Vehicle

## Setup and Running

### Prerequisites
- Java 17 or higher
- Apache Kafka (for messaging features)

### Running the Application
1. Clone the repository
2. Configure Kafka in `application.properties` if needed
3. Run the application:
   ```
   ./gradlew bootRun
   ```
4. Access the API at `http://localhost:8080`
5. Access the H2 console at `http://localhost:8080/h2-console`

### Running Tests
```
./gradlew test
```

## API Documentation
Once the application is running, you can access the OpenAPI documentation at:
`http://localhost:8080/swagger-ui.html`