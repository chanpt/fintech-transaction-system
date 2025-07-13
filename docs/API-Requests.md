# FinTech Transaction System – Sample API Requests

This document provides tested sample API requests and responses using `curl`. Use these to test the system manually or understand typical request/response patterns.

---
## 🧾 Account Service

### ✅ Create Account
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{
  "accountNumber": "J0717",
  "accountHolderName": "Jacky",
  "balance": 1000,
  "accountType": "BROKERAGE"
}'
```
#### Expected Response:
```json
{
    "id": 1,
    "accountNumber": "J0717",
    "accountHolderName": "Jacky",
    "balance": 1000,
    "accountType": "BROKERAGE"
}
```

### ✅ Get All Accounts
```bash
curl http://localhost:8080/accounts
```
#### Expected Response:
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

### ✅ Get Account by ID
```bash
curl http://localhost:8080/accounts/1
```
#### Expected Response:
```json
{
    "id": 1,
    "accountNumber": "J0717",
    "accountHolderName": "Jacky",
    "balance": 1000,
    "accountType": "BROKERAGE"
}
```

### ✅ Update Account by ID
```bash
curl -X PUT http://localhost:8080/accounts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "accountNumber": "J0717 Updated",
    "accountHolderName": "Jacky Updated",
    "balance": 1000,
    "accountType": "BROKERAGE"
}'
```
#### Expected Response:
```json
{
    "id": 1,
    "accountNumber": "J0717 Updated",
    "accountHolderName": "Jacky Updated",
    "balance": 1000,
    "accountType": "BROKERAGE"
}
```

### ✅ Delete Account by ID
```bash
curl -X DELETE http://localhost:8080/accounts/1
```
#### Expected Response `204 No Content`

## 💸 Transaction Service

### ✅ Create Transaction
```bash
curl -X POST http://localhost:8081/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}'
```
#### Expected Response:
```json
{
    "id": 1,
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}
```

### ✅ Get All Transactions
```bash
curl http://localhost:8081/transactions
```
#### Expected Response:
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
### ✅ Get Transaction by ID
```bash
curl http://localhost:8081/transactions/1
```
#### Expected Response:
```json
{
    "id": 1,
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}
```

### ✅ Delete Transaction by ID
```bash
curl -X DELETE http://localhost:8081/transactions/2
```
#### Expected Response `204 No Content`

## 💼 Balance Service

### ✅ Get Balance by Account Number
```bash
curl http://localhost:8082/balance/J0717
```
#### Expected Response:
```json
{
    "id": 1,
    "accountNumber": "J0717",
    "balance": 1000
}
```

## 🧪 Common Headers
All requests with a body must include:
```bash
-H "Content-Type: application/json"
```
Optional for response formatting:
```bash
-H "Accept: application/json"
```
---