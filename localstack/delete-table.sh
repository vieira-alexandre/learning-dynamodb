#!/bin/sh

LOCALSTACK_HOST=localhost
PORT=4566

echo "[INFO] deleting dynamodb table..."
aws dynamodb delete-table --endpoint-url=http://$LOCALSTACK_HOST:$PORT \
    --table-name Movies