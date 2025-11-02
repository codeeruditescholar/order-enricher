# Order Enricher

Order Enricher is a backend service designed to enrich order data. It is built with Java 21, uses the Spring Boot framework, and stores its data in a PostgreSQL database. The project includes sample API requests via a brunoCollection and can be run locally or via Docker.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Database:** PostgreSQL
- **API Testing:** Bruno Collection
- **Containerization:** Docker (optional)

## Setup Instructions

### Prerequisites
- Java 21+
- PostgreSQL (local or remote)
- (Optional) Docker & Docker Compose

### Environment Configuration

The application uses environment variables for database configuration. You can override them as needed. Typical variables include:



You can set these in your shell, in an `.env` file, or supply them as Docker environment variables.

### Local Development Setup

1. **Clone the repository:**
    ```sh
    git clone https://github.com/codeeruditescholar/order-enricher.git
    cd order-enricher
    ```

2. **Set up PostgreSQL database:**

   Create a database and user matching your environment variables.

    ```sql
    CREATE DATABASE orderdb;
    CREATE ROLE orderdbuser  WITH LOGIN PASSWORD 'orderdbpassword';
    ALTER DATABASE orderdb OWNER TO orderdbuser;
    GRANT ALL PRIVILEGES ON DATABASE orderdb TO orderdbuser;
    ```

3. **Run the application:**
   using Gradle:
    ```sh
    ./gradlew bootRun
    ```

4. **Access the service:**
   The API will run on `http://localhost:8080` by default.

## Running the Code

After setup, you can send HTTP requests to the API endpoints. See below for sample requests.

## Sample API Requests

The repository includes a Bruno collection with sample requests. Here are example `curl` commands:

### Health Check

curl http://localhost:8080/actuator/health

**Response:**


### Create Order

curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "123e4567-e89b-12d3-a456-426614174000",
    "customerId": "a1b2c3d4-e5f6-4a5b-8c9d-1e2f3a4b5c6d",
    "productIds": ["11111111-1111-1111-1111-111111111111"],
    "timestamp": "2025-12-01T14:30:00"
  }'

curl http://localhost:8080/orders/123e4567-e89b-12d3-a456-426614174000

For a complete list of requests, import the `brunoCollection` folder in Bruno or review its contents.

## Useful Information

- **Database Configuration:** All DB environment variables can be overridden.
- **Health Check:** `GET /actuator/health`
- **API Base Path:** All order endpoints are under `/orders`
- **Logs:** Output to console; adjust using `application.properties`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first.

## License

See the LICENSE file for details.

