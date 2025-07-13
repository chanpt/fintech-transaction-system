#!/bin/bash

curl -X POST http://localhost:8081/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderAccountNumber": "J0717",
    "receiverAccountNumber": "Y1207",
    "amount": 50,
    "description": "Fund transfer"
}'