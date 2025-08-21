# User Service REST API

Простое REST API приложение для управления пользователями и ролями.  
Стек: **Java 17**, **Spring Boot 3**, **PostgreSQL**, **Liquibase**, **Gradle (Kotlin DSL)**, **Swagger/OpenAPI 3**, **Docker/Docker Compose**, **Testcontainers**, **Hibernate**.

## 🚀 Функционал
- Добавление нового пользователя (ФИО, номер телефона, аватарка, роль)
- Получение информации о пользователе
- Обновление информации о пользователе
- Удаление пользователя по UUID (роль отвязывается)
- Документация API (Swagger UI)
- Валидация входящих данных
- Кэширование запросов
- Liquibase миграции и тестовые данные
- Тесты: unit и integration (с Testcontainers)
- скрипты для запуска сборки

---

## 📦 Запуск проекта

### 1. Сборка проекта и запус контейнеров

``для билда без тестов запустить это скрипт``

./scripts/run_without_tests.sh

``для билда с тестами запустить это скрипт``

./scripts/run.sh

### 2. тестирование через swagger

http://localhost:8080/swagger-ui.html


