#!/bin/bash
set -e

echo "🚀 Сборка проекта..."
./gradlew clean build -x test

echo "🐳 Запуск docker-compose..."
docker compose down && docker compose up --build
