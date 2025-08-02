# FinTech Transaction System - Microservices Architecture

## Overview

This is a microservices-based personal project designed to simulate core banking operations such as account management, transactions, and balance updates. The system is built using **Java**, **Spring Boot**, **Kafka**, **PostgreSQL**, and **Docker**.

## 🚀 Key Features 
### ✅ Account Service
- RESTful API to create, read, update, and delete bank accounts.
- On account creation, emits a Kafka event to create an initial balance. 

### 💸 Transaction Service
- RESTful API to perform debit/credit transactions.
- Validates account existence and balance before processing.
- Publishes Kafka events for balance updates. 

### 📬 Balance Service
- Listens to Kafka events: 
  - `BalanceCreateEvent` to initialize account balances. 
  - `BalanceUpdateEvent` to update balances upon transaction events. 
- Handles DEBIT and CREDIT operations with persistent storage.

## 🧱 Tech Stack
- Java 17
- Spring Boot
- PostgreSQL (relational database)
- Apache Kafka (event streaming)
- Docker & Docker Compose (containerization)
- JUnit + Mockito (unit testing)
- Maven (build tool)
- GitHub Actions (optional)

## 📐 System Architecture 
The system is designed using a microservices architecture. Each service is responsible for a specific business function and communicates with others through Apache Kafka. Each service also has its own database for data isolation and scalability. 

### Key Components:
- **Account Service** – Manages account creation and retrieval. 
- **Transaction Service** – Handles fund transfers between accounts and publishes transaction events.
- **Balance Service** – Listens for events and adjusts user balances. 
- **Kafka Broker** – Enables asynchronous, event-driven communication between services.
- **PostgreSQL** – Each microservice has its own PostgreSQL instance for data persistence.
  
## 🔧 Prerequisites

Ensure the following tools are installed on your machine:

- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker & Docker Compose](https://docs.docker.com/compose/)
  
## 🛠️ How to Run 
```bash
# Clone the project
git clone https://github.com/chanpt/fintech-transaction-system.git
cd fintech-transaction-system

# Start PostgreSQL (if you have Docker installed)
docker run --name fintech-postgres -e POSTGRES_PASSWORD=pass -e POSTGRES_USER=finuser -e POSTGRES_DB=fintech -p 5432:5432 -d postgres

# Run everything via Docker Compose
docker compose up -d
```
```bash
# Run the Spring Boot app for individual services
cd account-service && ./mvnw spring-boot:run
cd transaction-service && ./mvnw spring-boot:run
cd balance-service && ./mvnw spring-boot:run
```

## 🧪 Testing
- Unit-tested using JUnit 5 + Mockito
- Integration tests: Spring Boot Test + MockMvc + Embedded Kafka
- API documentation: Swagger UI / Postman

### Code coverage 
JaCoCo is used to measure the test coverage.

```bash
# Run
./mvnw clean verify
```
```bash
# Open the coverage report
open target/site/jacoco/index.html      # macOS
xdg-open target/site/jacoco/index.html  # Linux
```

### ✅ Implemented
- `account-service`, `transaction-service`, and `balance-service` are deployed and operational
- Balances are updated through Kafka-based event-driven communication after each transaction
- `account-service` now supports post-trade account types (e.g., BROKERAGE, CLEARING, CUSTODIAN)
- API documentation integrated via Swagger/OpenAPI
- Integration tests for services and controllers (MockMvc + Embedded Kafka)
- JaCoCo test coverage reporting enabled for all microservices

### 🛠️ Future Improvements (Optional Ideas)
Although development is currently frozen, the following improvements were previously considered and may be revisited:
- Position Service: Track security holdings or assets linked to accounts.
- Settlement Service: Simulate clearing and settlement of transactions.
- Authentication & Authorization: Implement user authentication.
- CI/CD Pipeline: Automate tests and deployments using Github Actions or Jenkins.
- Expanded unit and integration test coverage. 
