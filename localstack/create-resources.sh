#!/bin/sh

LOCALSTACK_HOST=localstack

waiting_resource() {
  resource=$1
  host=$2
  port=$3
  while ! nc -z $host $port; do
    echo "[INFO] Waiting for resource ${resource} in ${host}:${port}..."
    sleep 1
  done
}

waiting_resource "DYNAMO" $LOCALSTACK_HOST 4566
#waiting_resource "SQS" $LOCALSTACK_HOST 4576

echo "[INFO] creating dynamodb table..."
aws dynamodb create-table --endpoint-url=http://$LOCALSTACK_HOST:4566 \
    --cli-input-json file:///setup//dynamo-table.json