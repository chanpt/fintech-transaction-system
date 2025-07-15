# FinTech Transaction System - Microservices Architecture

## Overview

This is a backend microservices project simulating a basic FinTech transaction system.  
Built using **Java**, **Spring Boot**, **Kafka**, **PostgreSQL**, and **Docker**, it demonstrates event-driven architecture and service separation.

### 🔄 Recent Update

The system now includes support for **post-trade account types** (e.g., BROKERAGE, CUSTODIAN, CLEARING) as a foundational step toward modeling post-trade operations like settlement and reconciliation.  
Balances remain managed within the account service to keep the architecture simple and maintainable.

## 🚀 Key Faetures 
### ✅ Account Management
- Create and manage accounts
- Support for post-trade `accountType` (e.g., `BROKERAGE`, `CLEARING`, `CUSTODIAN`)
- Each account includes a `balance` field for simplicity

### 💸 Transactions
- Create transactions with sender and receiver accounts
- Deduct and credit balances accordingly
- Uses Kafka to publish transaction events

### 📬 Event-Based Architecture
- Kafka topic: 
  - `balance-update-topic` for balance change events
  - `balance-create-topic` for balance create events
- Future Kafka events: `account-created`, `trade-confirmed`, `settlement-completed`

## 🧱 Tech Stack
- Java 17
- Spring Boot
- PostgreSQL (relational database)
- Apache Kafka (event streaming)
- Docker & Docker Compose (containerization)
- JUnit + Mockito (Unit Testing)
- Maven (build tool)
- Github Actions (optional)

## 📐 System Architecture 
The system is designed using a microservices architecture. Each service is responsible for a specific business function and communicates with others through Apache Kafka. Each service also has its own database for data isolation and scalability. 

### Key Components:
- **Account Service** – Manages account creation and retrieval. Now supports `accountType` (BROKERAGE, CUSTODIAN, etc.).
- **Transaction Service** – Handles fund transfers between accounts and publishes transaction events.
- **Balance Service** – Listens for events and adjusts user balances. (WIP for decoupled balance tracking)
- **Kafka Broker** – Enables asynchronous, event-driven communication between services.
- **PostgreSQL** – Each microservice has its own PostgreSQL instance for data persistence.
  
## 🔧 Prerequisites

Make sure the following tools are installed on your machine:

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

# Run Everything via Docker Compose
docker compose up -d
```
```bash
# Run the Spring Boot app for individual services
cd account-service && ./mvnw spring-boot:run
cd transaction-service && ./mvnw spring-boot:run
cd transaction-service && ./mvnw spring-boot:run
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

## 📌 Status
Currently building and documenting the system. The project is under active development with core services already functional.

### ✅ Implemented
- `account-service`, `transaction-service`, and `balance-service` are deployed and operational
- Balance is updated through Kafka-based event-driven communication after each transaction
- `account-service` now supports post-trade account types (e.g., BROKERAGE, CLEARING, CUSTODIAN)
- API documentation integrated via Swagger/OpenAPI
- Integration tests for services and controllers (MockMvc + Embedded Kafka)
- JaCoCo test coverage reporting enabled for all microservices

### 🔄 In Progress
- `position-service` for securities holdings per account based on executed transactions (e.g., BUY/SELL)
- Kafka event schemas and documentation

### 🚧 Planned
- `settlement-service` to simulate post-trade lifecycle (confirmations, cash movements, settlement)
- Kafka events for trade creation, settlement confirmation, and reconciliation
- Expanded unit and integration test coverage 
