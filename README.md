# Smart Subscription Manager

A microservice-based backend application for managing subscriptions, tracking recurring expenses, configuring personal budgets, and forecasting future spending.

The project is built around an event-driven architecture where business events are published to Apache Kafka and consumed asynchronously by downstream services.

## Architecture

```text
                           ┌─────────────────────┐
                           │       Client        │
                           └──────────┬──────────┘
                                      │
                                      ▼
                           ┌─────────────────────┐
                           │     API Gateway     │
                           │                     │
                           │  • JWT validation   │
                           │  • Request routing  │
                           └───────┬───────┬─────┘
                                   │       │
                    Auth requests  │       │ Protected requests
                                   ▼       ▼
                         ┌────────────┐  ┌─────────────┐
                         │   Auth     │  │    Core     │
                         │  Service   │  │   Service   │
                         │            │  │             │
                         │ • Register │  │ • Users     │
                         │ • Login    │  │ • Subs      │
                         │ • JWT      │  │ • Budgets   │
                         └─────┬──────┘  └──────┬──────┘
                               │                │
                               │                │ Domain events
                               │                ▼
                               │        ┌──────────────────┐
                               └───────►│      Kafka       │
                                        │                  │
                                        │ • Avro events    │
                                        │ • Schema Registry│
                                        └────────┬─────────┘
                                                 │
                                                 ▼
                                        ┌──────────────────┐
                                        │ Billing Service  │
                                        │                  │
                                        │ • Forecasting    │
                                        │ • Expense calc.  │
                                        │ • Budget status  │
                                        └────────┬─────────┘
                                                 │
                                                 │ OpenFeign
                                                 ▼
                                        ┌──────────────────┐
                                        │   Core Service   │
                                        │                  │
                                        │ Current domain   │
                                        │ data retrieval   │
                                        └──────────────────┘
```

### Service responsibilities

| Service             | Responsibility                                          |
| ------------------- | ------------------------------------------------------- |
| **API Gateway**     | Single entry point, JWT validation and request routing  |
| **Auth Service**    | User registration, authentication and JWT issuance      |
| **Core Service**    | Users, subscriptions, budgets and domain business logic |
| **Billing Service** | Expense calculation and subscription cost forecasting   |
| **Config Server**   | Centralized application configuration                   |
| **Event Schemas**   | Shared Apache Avro event contracts                      |

## Communication

The application uses both synchronous and asynchronous communication.

### Synchronous communication

Client requests are routed through the API Gateway.

```text
Client
   │
   ▼
API Gateway
   │
   ▼
Core Service
```

When Billing Service needs current domain data, it communicates with Core Service using OpenFeign:

```text
Billing Service
      │
      │ OpenFeign
      ▼
Core Service
```

### Asynchronous communication

Services communicate through Apache Kafka using Avro-based events.

```text
Auth Service
     │
     │ UserRegisteredEvent
     ▼
   Kafka
     │
     ▼
Core Service
```

Core Service publishes business events:

```text
Core Service
     │
     │ Domain Events
     ▼
   Kafka
     │
     ▼
Billing Service
```

There is **no direct communication between Auth Service and Billing Service**.

## Authentication

Authentication is based on JWT.

### Login flow

```text
Client
   │
   ▼
API Gateway
   │
   ▼
Auth Service
   │
   ├── Validate credentials
   │
   ├── Generate access token
   │
   └── Generate refresh token
   │
   ▼
Client
```

Auth Service is responsible for issuing tokens.

The API Gateway independently validates JWT tokens for protected requests.

```text
Client
   │
   │ JWT
   ▼
API Gateway
   │
   ├── Validate JWT
   ├── Extract user information
   │
   ▼
Downstream Service
```

The Gateway does **not** call Auth Service to validate every JWT.

Refresh tokens are stored in Redis.

## Event-Driven Architecture

Apache Kafka is used to decouple services and process business events asynchronously.

The project uses Apache Avro for strongly typed event contracts and Confluent Schema Registry for schema management.

### Events

| Event                        | Producer     | Consumer        |
| ---------------------------- | ------------ | --------------- |
| `UserRegisteredEvent`        | Auth Service | Core Service    |
| `SubscriptionCreatedEvent`   | Core Service | Billing Service |
| `SubscriptionUpdatedEvent`   | Core Service | Billing Service |
| `SubscriptionActivatedEvent` | Core Service | Billing Service |
| `SubscriptionCancelledEvent` | Core Service | Billing Service |
| `SubscriptionDeletedEvent`   | Core Service | Billing Service |
| `BudgetSettingsUpdatedEvent` | Core Service | Billing Service |

Event schemas are stored in the dedicated `event-schemas` module:

```text
event-schemas/
└── src/
    └── main/
        └── avro/
            ├── UserRegisteredEvent.avsc
            ├── SubscriptionCreatedEvent.avsc
            ├── SubscriptionUpdatedEvent.avsc
            ├── SubscriptionActivatedEvent.avsc
            ├── SubscriptionCancelledEvent.avsc
            ├── SubscriptionDeletedEvent.avsc
            └── BudgetSettingsUpdatedEvent.avsc
```

This allows services to share stable event contracts without being tightly coupled to each other's Java implementation.

## Outbox Pattern

The project uses the **Transactional Outbox Pattern** for reliable event publishing.

Instead of publishing an event directly to Kafka inside the business operation, the event is first stored in the database as part of the same transaction.

```text
Business Operation
       │
       ▼
┌─────────────────────┐
│ Database Transaction│
│                     │
│  Domain Data        │
│  +                  │
│  Outbox Event       │
└──────────┬──────────┘
           │
           ▼
     Event Publisher
           │
           ▼
         Kafka
```

This helps prevent situations where a database transaction succeeds but the corresponding Kafka event is lost.

## Kafka Reliability

Kafka consumers include failure-handling mechanisms.

### Duplicate event protection

Processed events are tracked to prevent the same event from being processed multiple times.

```text
Kafka Event
     │
     ▼
Check processed events
     │
 ┌───┴────┐
 │        │
 ▼        ▼
Already   New
processed event
 │        │
 ▼        ▼
Skip    Process
```

### Retry and Dead Letter Topics

Failed Kafka messages are retried.

If processing continues to fail, the message can be sent to a Dead Letter Topic for further investigation.

```text
Kafka
  │
  ▼
Consumer
  │
  ├── Success ─────────► Done
  │
  └── Failure
        │
        ▼
      Retry
        │
        └── Failure
              │
              ▼
             DLT
```

Spring Kafka's `DefaultErrorHandler` and `DeadLetterPublishingRecoverer` are used for this mechanism.

## Business Flow

### 1. User Registration

```text
Client
  │
  ▼
API Gateway
  │
  ▼
Auth Service
  │
  ├── Create authentication data
  │
  └── Publish UserRegisteredEvent
              │
              ▼
             Kafka
              │
              ▼
         Core Service
              │
              └── Create user profile
```

### 2. Subscription Creation

```text
Client
  │
  ▼
API Gateway
  │
  ▼
Core Service
  │
  ├── Validate request
  ├── Save subscription
  └── Create domain event
              │
              ▼
         Outbox Event
              │
              ▼
            Kafka
              │
              ▼
       Billing Service
              │
              └── Recalculate forecast
```

The same event-driven approach is used for subscription updates, activation, cancellation and deletion.

### 3. Budget Settings Update

```text
Client
  │
  ▼
API Gateway
  │
  ▼
Core Service
  │
  ├── Update budget settings
  └── Publish BudgetSettingsUpdatedEvent
                 │
                 ▼
                Kafka
                 │
                 ▼
          Billing Service
                 │
                 └── Recalculate forecast
```

## Expense Forecasting

Billing Service calculates expected future subscription expenses based on the current user data.

When required, Billing Service retrieves the latest data from Core Service using OpenFeign.

```text
Billing Service
      │
      ├── Get budget settings ───────┐
      │                              │
      ├── Get active subscriptions ──┤
      │                              ▼
      │                         Core Service
      │                              │
      └────────────── Data ◄─────────┘
                     │
                     ▼
              Forecast calculation
                     │
                     ▼
              Budget evaluation
```

Supported billing periods include:

* Weekly
* Monthly
* Yearly

Budget status is calculated based on forecasted expenses:

* `OK`
* `WARNING`
* `EXCEEDED`

The forecasting logic is isolated in Billing Service so that financial calculations do not become part of the Core Service.

## Data Storage

### PostgreSQL

PostgreSQL is used as the primary relational database.

It stores:

* Users
* User profiles
* Subscriptions
* Budget settings
* Outbox events
* Processed event information
* Other application data

### Redis

Redis is used for temporary and fast-access data, including refresh token storage.

## Database Migrations

Database schema changes are managed using **Flyway**.

Migrations are versioned and applied automatically when the corresponding service starts.

Example:

```text
src/main/resources/db/migration/
├── V1__create_users.sql
├── V2__create_subscriptions.sql
├── V3__create_budget_settings.sql
└── ...
```

## Configuration

The project uses **Spring Cloud Config Server** for centralized configuration.

```text
                    ┌──────────────────┐
                    │   Config Server  │
                    └─────────┬────────┘
                              │
                ┌─────────────┼─────────────┐
                ▼             ▼             ▼
           Auth Service  Core Service  Billing Service
```

This keeps service-specific configuration outside the application code and allows configuration to be managed centrally.

Sensitive values such as JWT secrets should be provided through environment variables and should not be committed to the repository.

## Project Structure

```text
smart-subscription-manager/
│
├── api-gateway/
│   └── ...
│
├── auth-service/
│   └── ...
│
├── core-service/
│   └── ...
│
├── billing-service/
│   └── ...
│
├── config-server/
│   └── ...
│
├── event-schemas/
│   └── src/main/avro/
│       ├── UserRegisteredEvent.avsc
│       ├── SubscriptionCreatedEvent.avsc
│       ├── SubscriptionUpdatedEvent.avsc
│       ├── SubscriptionActivatedEvent.avsc
│       ├── SubscriptionCancelledEvent.avsc
│       ├── SubscriptionDeletedEvent.avsc
│       └── BudgetSettingsUpdatedEvent.avsc
│
├── docker/
│   └── Dockerfile
│
├── docker-compose.yml
├── build.gradle
├── settings.gradle
└── README.md
```

## Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* Spring Cloud Gateway
* Spring Cloud Config
* OpenFeign

### Messaging

* Apache Kafka
* Apache Avro
* Confluent Schema Registry
* Spring Kafka

### Databases

* PostgreSQL
* Redis
* Flyway

### Build & Infrastructure

* Gradle
* Docker
* Docker Compose

### Testing

* JUnit 5
* Mockito
* Spring Boot Test
* Spring Security Test
* Testcontainers

## Testing

The project uses unit and integration tests.

Testcontainers is used for integration testing with real infrastructure dependencies such as PostgreSQL.

Example test stack:

```text
JUnit 5
   │
   ▼
Spring Boot Test
   │
   ▼
Testcontainers
   │
   └── PostgreSQL
```

This allows integration tests to run against an environment closer to the real application infrastructure.

## Running the Project

### Prerequisites

Make sure the following are installed:

* Java 21
* Docker
* Docker Compose
* Git

### 1. Clone the repository

```bash
git clone <repository-url>
cd smart-subscription-manager
```

### 2. Configure environment variables

Create a `.env` file if your Docker Compose configuration expects environment variables.

Provide the values required by the project, including secrets and environment-specific configuration.

Do not commit `.env` or other files containing secrets.

### 3. Start the application

Build and start all services:

```bash
docker compose up -d --build
```

Check running containers:

```bash
docker compose ps
```

### 4. Check application logs

To view logs from all services:

```bash
docker compose logs -f
```

To view logs of a specific service:

```bash
docker compose logs -f api-gateway
```

or:

```bash
docker compose logs -f auth-service
```

### 5. Stop the application

```bash
docker compose down
```

To remove containers and associated volumes:

```bash
docker compose down -v
```

Use the `-v` option carefully because it removes persistent Docker volumes, including database data.

## Local Development

The project can also be built with Gradle.

Build the project:

```bash
./gradlew build
```

Run tests:

```bash
./gradlew test
```

On Windows:

```bash
gradlew.bat build
```

```bash
gradlew.bat test
```

Infrastructure services such as PostgreSQL, Redis, Kafka and Schema Registry can be started using Docker Compose while individual Spring Boot services are run from the IDE.

## Design Principles

The project demonstrates several backend and distributed-system concepts:

* Microservice architecture
* Event-driven architecture
* Asynchronous communication with Kafka
* Synchronous service-to-service communication with OpenFeign
* JWT-based authentication
* API Gateway pattern
* Transactional Outbox Pattern
* Idempotent event processing
* Kafka retry and Dead Letter Topics
* Avro event contracts
* Schema Registry
* Centralized configuration
* Database migrations with Flyway
* Integration testing with Testcontainers
* Docker-based local environment

## Key Architectural Decisions

### Why Kafka?

Kafka decouples services and allows business events to be processed asynchronously.

For example, Core Service does not need to wait for Billing Service to finish recalculating expenses before completing a subscription operation.

### Why Avro?

Avro provides strongly defined schemas for Kafka messages.

Instead of relying on arbitrary JSON structures, producers and consumers share explicit event contracts.

### Why Schema Registry?

Schema Registry manages event schemas and helps maintain compatibility between different versions of producers and consumers.

### Why OpenFeign?

Some operations require fresh synchronous data rather than an asynchronous event.

Billing Service therefore uses OpenFeign to request the current state from Core Service when calculating forecasts.

### Why Outbox Pattern?

The Outbox Pattern helps keep database state changes and event publication consistent.

The business operation and creation of the outgoing event are performed within the same database transaction.

## Conclusion

Smart Subscription Manager is a microservice-based backend designed to demonstrate practical distributed-system patterns rather than a simple CRUD application.

The architecture combines synchronous REST communication with asynchronous Kafka events:

```text
                    SYNCHRONOUS

Client → API Gateway → Core Service
                         ▲
                         │
                    OpenFeign
                         │
                  Billing Service


                    ASYNCHRONOUS

Auth Service ──► Kafka ──► Core Service

Core Service ──► Kafka ──► Billing Service
```

The project focuses on reliable event-driven communication, service separation, authentication at the API Gateway level, centralized configuration, and independent business responsibilities across microservices.

