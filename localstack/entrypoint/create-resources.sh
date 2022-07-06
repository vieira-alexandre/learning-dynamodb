#!/bin/sh

LOCALSTACK_HOST=localstack


echo "[INFO] creating dynamodb table..."

aws dynamodb create-table --endpoint-url=http://$LOCALSTACK_HOST:4566 \
    --cli-input-json file:///setup//dynamo-table-movies.json

echo "[INFO] table crated..."
