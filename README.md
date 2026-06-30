# Smart Subscription Manager

Микросервисная система для управления подписками, бюджетами и биллингом с event-driven архитектурой на Kafka.

---

# 🧩 Архитектура проекта

Система построена на микросервисах с асинхронным взаимодействием через Kafka и синхронными вызовами через Feign.

### Микросервисы:

| Сервис | Назначение | Технологии |
|--------|------------|-------------|
| **auth-service** | Аутентификация и регистрация пользователей | Spring Security, JWT, Kafka, PostgreSQL |
| **core-service** | Основная бизнес-логика (подписки, профили, бюджеты) | Spring Boot, JPA, Kafka, PostgreSQL |
| **billing-service** | Расчёт расходов и прогноз бюджета | Spring Boot, Kafka, Feign, PostgreSQL |
| **config-server** | Централизованная конфигурация | Spring Cloud Config |

---

# 🔐 Пользовательские статусы

| Enum | Значения | Назначение |
|------|----------|------------|
| **UserStatus** | ACTIVE, BLOCKED | Статус аккаунта |
| **SubscriptionCategory** | MUSIC, CLOUD, GAMING, AI, EDUCATION, OTHER | Категория подписки |
| **BillingPeriod** | WEEKLY, MONTHLY, YEARLY | Период списания |
| **SubscriptionStatus** | ACTIVE, CANCELLED | Статус подписки |
| **BudgetStatus** | OK, WARNING, EXCEEDED | Статус бюджета |

---

# 📡 Kafka Events

| Event | Producer | Consumer | Назначение |
|-------|----------|----------|-------------|
| UserRegisteredEvent | auth-service | core-service | Создание профиля и бюджета |
| SubscriptionCreatedEvent | core-service | billing-service | Расчёт прогноза |
| SubscriptionUpdatedEvent | core-service | billing-service | Пересчёт после изменений |
| SubscriptionCancelledEvent | core-service | billing-service | Исключение из прогноза |
| BudgetSettingsUpdatedEvent | core-service | billing-service | Пересчёт статуса бюджета |

---

# 🔗 Feign взаимодействия

| Сервис | Вызывает | Назначение |
|--------|----------|------------|
| billing-service | core-service | Получение настроек бюджета |
| billing-service | core-service | Получение активных подписок |
| billing-service | core-service | Получение профиля пользователя |

---

# ⚙️ Основные бизнес-процессы

## 🧑 Регистрация
- Создание credentials
- Публикация `UserRegisteredEvent`
- Создание профиля и бюджета

---

## 🔑 Логин
- Выдача JWT
- Сохранение refresh token в Redis

---

## 💳 Создание подписки
- Валидация данных
- Расчёт nextPaymentDate
- Публикация события

---

## 🔄 Обновление подписки
- Изменение параметров
- Пересчёт бюджета
- Публикация события

---

## ❌ Отмена подписки
- Изменение статуса
- Исключение из прогнозов

---

## 💰 Изменение бюджета
- Обновление лимитов
- Пересчёт BudgetStatus

---

## 📊 Расчёт прогноза
- Сбор подписок пользователя
- Расчёт расходов
- Обновление BudgetStatus

---

# 🧠 Ключевые принципы архитектуры

- Event-driven коммуникация через Kafka
- Outbox pattern для гарантированной доставки событий
- Idempotent consumers
- Разделение ответственности между сервисами
- Минимизация синхронных зависимостей

---

# 🚀 Outbox Pattern (используется в core/auth)

- События сначала пишутся в БД
- Затем публикуются в Kafka через scheduler
- Используется `FOR UPDATE SKIP LOCKED`
- Гарантия at-least-once delivery

---

# 📦 Технологический стек

- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- Kafka
- PostgreSQL
- Feign Client
- Redis
- Flyway
- Docker

---

# 📌 Цель проекта

Создать масштабируемую систему управления подписками с:

- устойчивой event-driven архитектурой
- гарантированной доставкой событий
- разделением доменных сервисов
- поддержкой прогнозирования расходов

---

# 🧱 Примечание

Проект ориентирован на практику микросервисной архитектуры

