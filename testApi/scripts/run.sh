#!/usr/bin/env bash
set -euo pipefail

chmod +x scripts/*.sh || true
./gradlew clean build
docker-compose down && docker-compose up --build
echo "Service should be available at http://localhost:8080/swagger-ui.html"
