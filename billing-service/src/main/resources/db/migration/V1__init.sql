CREATE SCHEMA IF NOT EXISTS billing_service;

CREATE TABLE IF NOT EXISTS expense_forecasts
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID            NOT NULL UNIQUE,
    monthly_expenses NUMERIC(10, 2)  DEFAULT 0,
    remaining_budget NUMERIC(10, 2)  DEFAULT 0,
    status           VARCHAR(30),
    calculated_at    TIMESTAMP      NOT NULL
);

CREATE TABLE processed_events
(
    event_id     UUID PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

