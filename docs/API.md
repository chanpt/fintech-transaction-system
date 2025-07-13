# FinTech Transaction System - Microservices Architecture

## Overview

This system provides APIs to manage accounts, balances, and financial transactions. It follows an event-driven microservices architecture using Spring Boot, Kafka, and PostgreSQL.

- Architecture: Microservices + Kafka + REST
- Services: Account Service, Transaction Service, Balance Service
- Format: JSON RESTful APIs

---

## 🧾 Account Service 
**Base URL:** `/accounts`

### `POST /accounts`
Create a new user account.

#### Request
```json
{
  "accountNumber": "J0717",
  "accountHolderName": "Jacky",
  "balance": 1000,
  "accountType": "BROKERAGE"
}
```
#### Response `201 Created`
```json
{
    "id": 1,
    "accountNumber": "J0717",
    "accountHolderName": "Jacky",
    "balance": 1000,
    "accountType": "BROKERAGE"
}
```

### `GET /accounts`
Fetch all accounts.

#### Response `200 OK`
```json
[
    {
        "id": 1,
        "accountNumber": "J0717",
        "accountHolderName": "Jacky",
        "balance": 1000,
        "accountType": "BROKERAGE"
    },
    {
        "id": 2,
        "accountNumber": "H1207",
        "accountHolderName": "Yuzu",
        "balance": 2000,
        "accountType": "OTHER"
    }
]
```

### `GET /accounts/{id}`
Fetch an account by ID.

#### Response `200 OK`
```json
{
    "id": 2,
    "accountNumber": "H1207",
    "accountHolderName": "Yuzu",
    "balance": 2000,
    "accountType": "OTHER"
}
```
#### Response `404 Not Found`

### `PUT /accounts/{id}`
Update an account.

#### Request
```json
{
    "id": 1,
    "accountNumber": "J0717 Updated",
    "accountHolderName": "Jacky Updated",
    "balance": 1000,
    "accountType": "BROKERAGE"
}
```
#### Response `200 OK`
```json
{
    "id": 1,
    "accountNumber": "J0717 Updated",
    "accountHolderName": "Jacky Updated",
    "balance": 1000,
    "accountType": "BROKERAGE"
}
```

### `DELETE /accounts/{id}`
Delete an account by ID (soft delete or permanent).

#### Response `204 No Content`

---

## 💸 Transaction Service
**Base URL:** `/transactions`

### `POST /transactions`
Create a new financial transaction.

#### Request
```json
{
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}
```
#### Response `201 Created`
```json
{
    "id": 1,
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}
```

### `GET /transactions`
List all transactions.

#### Response `200 OK`
```json
[
    {
        "id": 1,
        "senderAccountNumber": "J0717",
        "receiverAccountNumber": "Y1207",
        "amount": 50,
        "description": "Fund transfer"
    },
    {
        "id": 2,
        "senderAccountNumber": "E1114",
        "receiverAccountNumber": "A001",
        "amount": 100,
        "description": "Fee"
    }
]
```

### `GET /transactions/{id}`
Get transaction details by ID.

#### Response `200 OK`
```json
{
    "id": 1,
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}
```
#### Response `404 Not Found`

### `DELETE /transactions/{id}`
Delete a transaction by ID (soft delete or permanent).

#### Response `204 No Content`
---

## 💼 Balance Service
**Base URL:** `/balance`

### `GET /balance/{accountNumber}`
Get balance for an account.

#### Response `200 OK`
```json
{
    "id": 1,
    "accountNumber": "J0717",
    "balance": 1000
}
```
#### Response `404 Not Found`
💡 This service listens for Kafka balance update events.

---

