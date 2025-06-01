#!/bin/bash

SONAR_URL="http://localhost:9000"
SONAR_TOKEN="squ_3d07cad183e84de84bf66e7ba0c3e59fffa6cf78"

# 1. Создать новый Quality Gate
curl -X POST -u $SONAR_TOKEN: "$SONAR_URL/api/qualitygates/create?name=CustomGate"

# 2. Добавить условие "Coverage < 80% → ERROR"
curl -u $SONAR_TOKEN: \
  "$SONAR_URL/api/qualitygates/create_condition" \
  -d "gateName=CustomGate" \
  -d "metric=coverage" \
  -d "op=LT" \
  -d "error=80"

# 3. Назначить этот Quality Gate как default (по умолчанию)
curl -u $SONAR_TOKEN: \
  "$SONAR_URL/api/qualitygates/set_as_default" \
  -d "name=CustomGate"
