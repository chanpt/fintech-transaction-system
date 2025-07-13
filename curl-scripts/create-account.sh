#!/bin/bash

curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{
  "accountNumber": "J0717",
  "accountHolderName": "Jacky",
  "balance": 1000,
  "accountType": "BROKERAGE"
}'