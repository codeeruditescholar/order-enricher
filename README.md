# Order Enricher

Order Enricher is a backend service designed to enrich order data by fetching customer and product details. Built with Java 21 and Spring Boot, it stores enriched orders in a PostgreSQL database. The project includes a Bruno collection for API testing and supports local and Docker deployments.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5.5
- **Database:** PostgreSQL with JSONB support
- **Build Tool:** Gradle
- **API Testing:** Bruno Collection
- **Containerization:** Docker (optional)

## Features

- Create enriched orders with customer and product data
- Retrieve orders by ID
- Health check endpoint for monitoring
- JSONB storage for flexible customer and product snapshots
- Virtual threads for improved performance

## Setup Instructions

### Prerequisites

- Java 21 or higher
- PostgreSQL 12+ (local or remote)
- Docker (optional, for containerized deployment)

### Environment Configuration

The application reads database configuration from environment variables with sensible defaults:

```shell
properties db_host=localhost db_port=5432 db_username=orderdbuser db_password=orderdbpassword

```

You can set these in your shell:

```shell
sh export db_host=localhost export db_port=5432 export db_username=orderdbuser export db_password=orderdbpassword

```

Or create a `.env` file for Docker deployment.

### Local Development

1. **Clone the repository:**

    ```sh
    git clone https://github.com/codeeruditescholar/order-enricher.git
    cd order-enricher
    ```

2. **Prepare PostgreSQL database:**

   Connect to PostgreSQL and run:

    ```sql
    CREATE DATABASE orderdb;
    CREATE ROLE orderdbuser WITH LOGIN PASSWORD 'orderdbpassword';
    ALTER DATABASE orderdb OWNER TO orderdbuser;
    GRANT ALL PRIVILEGES ON DATABASE orderdb TO orderdbuser;
    ```

3. **Build and run the application:**

    ```sh
    ./gradlew bootRun
    ```

4. **Verify the service is running:**

    ```sh
    curl http://localhost:8080/actuator/health
    ```

   Expected response:

    ```json
    {
      "status": "UP"
    }
    ```

### Docker Deployment

1. **Build the application JAR:**

    ```sh
    ./gradlew :app:bootJar
    ```

2. **Prepare the JAR for Docker:**

    ```sh
    mkdir -p build/libs
    cp app/build/libs/*.jar build/libs/app.jar
    ```

3. **Build the Docker image:**

    ```sh
    docker build -t order-enricher:latest .
    ```

4. **Run the container:**

    ```sh
    docker run --rm -p 8080:8080 \
      -e db_host=host.docker.internal \
      -e db_port=5432 \
      -e db_username=orderdbuser \
      -e db_password=orderdbpassword \
      order-enricher:latest
    ```

   **Note:** On Linux, replace `host.docker.internal` with your host machine's IP address (e.g., `172.17.0.1` or your network IP).

## API Documentation

**Base URL:** `http://localhost:8080`

All endpoints accept and return JSON. Timestamps follow ISO-8601 format, and IDs are UUIDs.

### Health Check

Check if the service is running and healthy.

**Endpoint:**

```
GET /actuator/health

```

**Example:**

```shell
sh curl [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

```

**Response:**

```
json { "status": "UP" }

```

---

### Create Order

Create a new enriched order by providing order details with customer and product IDs. The service will fetch and store customer and product information.

**Endpoint:**

```
POST /orders

```

**Request Body:**

```json
{ "orderId": "123e4567-e89b-12d3-a456-426614174000", "customerId": "a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d", "productIds": ["11111111-1111-1111-1111-111111111111"], "timestamp": "2025-12-01T14:30:00" }

```

**Example:**

```shell
sh curl -X POST [http://localhost:8080/orders](http://localhost:8080/orders)
-H "Content-Type: application/json"
-d '{ "orderId": "123e4567-e89b-12d3-a456-426614174000", "customerId": "a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d", "productIds": ["11111111-1111-1111-1111-111111111111"], "timestamp": "2025-12-01T14:30:00" }'

```

**Response:** `201 Created`

```json
{ "id": "123e4567-e89b-12d3-a456-426614174000", "customer": { "id": "a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d", "name": "John Doe", "address": { "street": "123 Main St", "city": "Springfield", "state": "IL", "zip": "62701", "country": "USA" } }, "products": , "timestamp": "2025-12-01T14:30:00" }
```

---

### Get Order by ID

Retrieve an enriched order by its ID.

**Endpoint:**

```
GET /orders/{orderId}

```

**Example:**

```shell

sh curl [http://localhost:8080/orders/123e4567-e89b-12d3-a456-426614174000](http://localhost:8080/orders/123e4567-e89b-12d3-a456-426614174000)

```

**Response:** `200 OK`

```json
{ "id": "123e4567-e89b-12d3-a456-426614174000", "customer": { "id": "a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d", "name": "John Doe", "address": { "street": "123 Main St", "city": "Springfield", "state": "IL", "zip": "62701", "country": "USA" } }, "products": , "timestamp": "2025-12-01T14:30:00" }
```

---

### Error Responses

The API returns appropriate HTTP status codes and error messages:

- `404 Not Found` - Order, customer, or product not found
- `400 Bad Request` - Invalid request format
- `500 Internal Server Error` - Server error

Example error response:

```json
{
  "message": "Order not found",
  "timestamp": "2025-12-01T14:30:00"
}
```

## Bruno Collection

The project includes a Bruno collection with ready-to-use API requests located in the `brunoCollection` folder.
**To use:**

1. Open Bruno
2. Import the `brunoCollection` folder
3. Run the included requests:
    - Health Check
    - Create Order
    - Get Order
    - Error scenarios (Missing Customer, Missing Product, Missing Order)

## Testing

Run tests with coverage:

```

./gradlew test
```

View coverage report:

```
./gradlew jacocoTestReport
open build/jacocoHtml/index.html
```

## Project Structure

order-enricher/
├── adapter/ # External adapters (future integrations)
├── api/ # REST controllers and exception handlers
├── app/ # Main application and configuration
├── brunoCollection/ # API testing collection
├── common/ # Domain models and DTOs
├── repository/ # Database repositories
├── service/ # Business logic
└── gradle/ # Gradle wrapper files

## Additional Information

- **Server Port:** 8080 (default)
- **Database Schema:** Auto-updated via Hibernate DDL
- **Logging:** Console output, SQL queries visible in development
- **Virtual Threads:** Enabled for improved concurrency
- **Health Endpoint:** `/actuator/health`

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
