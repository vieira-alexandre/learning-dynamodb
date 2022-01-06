#!/bin/sh

LOCALSTACK_HOST=localhost
PORT=4566

echo "[INFO] creating dynamodb table..."
aws dynamodb create-table --endpoint-url=http://$LOCALSTACK_HOST:$PORT \
    --cli-input-json file://dynamo-table.json