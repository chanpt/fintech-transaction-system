# fintech-transaction-system
A backend microservices system to simulate financial transactions. Built with Java, Sprint Boot, PostgreSQL, and Docker. 

## 🚀 Key Faetures (Planned & In Progress)
- [x] Account microservice (create & retrieve accounts)
- [x] Transaction microservice (transfer funds between accounts)
- [x] Balance microservice (track and update balances)
- [ ] Notification microservice (send transaction alerts)
- [x] Kafka-based event-driven communication
- [x] Docker-based deployment
- [ ] Swagger API documentation
- [ ] Github Actions CI/CD pipeline

## 🧱 Tech Stack
- Java 17
- Spring Boot
- PostgreSQL
- Apache Kafka
- Docker & Docker Compose
- JUnit + Mockito
- Github Actions (optional)

## 📐 System Architecture 
The system is designed using a microservices architecture. Each service is responsible for a specific business function and communicates with others through Apache Kafka. Each service also has its own database for data isolation and scalability. 

### Key Components:
- **Account Service** - Manages account creation and retrieval
- **Transaction Service** - Handles fund transfers between accounts
- **Balance Service** - Tracks and updates user balance
- **Notification Service** - Sneds alerts on successful transactions
- **Kafka Broker** - Enables event-driven communication between services
- **PostgreSQL** - Each microservice uses its own instance for persistence
  
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
# Run the Spring Boot app
# Run for account service
cd account-service
./mvnw spring-boot:run
```


```bash
# Run for transaction service
cd transaction-service
./mvnw spring-boot:run
```

```bash
# Run for balance service
cd transaction-service
./mvnw spring-boot:run
```

## 📬 API Endpoints
### Account Microservice
| Method | Endpoint          | Description          |
|--------|-------------------|----------------------|
| POST   | `/accounts`       | Create new account   |
| GET    | `/accounts`       | Get all accounts     |
| GET    | `/accounts/{id}`  | Get account details  |
| PUT    | `/accounts/{id}`  | Update account       |
| DELETE | `/accounts/{id}`  | Delete account       |

### Transaction Microservice
| Method | Endpoint             | Description             |
|--------|----------------------|-------------------------|
| POST   | `/transactions`      | Create new transaction  |
| GET    | `/transactions`      | Get all transaction     |
| GET    | `/transactions/{id}` | Get transaction by ID   |

### Balance Microservice
| Method | Endpoint                   | Description                  |
|--------|----------------------------|------------------------------|
| GET    | `/balance/{accountNumber}`  | Get balance by acount number |

## 🧪 Testing
- Unit-tested using JUnit 5 + Mockito

## 📌 Status
Currently building and documenting the system
- Account service, transaction service and balance service are deployed
- Update balance after a transaction though Kafka-based event-driven communication
- More services and Kafka integration are planned
