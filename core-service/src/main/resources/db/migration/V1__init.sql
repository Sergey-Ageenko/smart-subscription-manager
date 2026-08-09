CREATE SCHEMA IF NOT EXISTS core_service;

CREATE TABLE profiles
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL UNIQUE,
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(80) NOT NULL

);

CREATE TABLE budgets
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    monthly_limit NUMERIC(12, 2) NOT NULL check (monthly_limit >= 0),
    profile_id    UUID           NOT NULL UNIQUE,

    CONSTRAINT fk_budget_profile
        FOREIGN KEY (profile_id)
            REFERENCES profiles (id)
            ON DELETE CASCADE

);

CREATE TABLE subscriptions
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name     VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS profile_subscriptions
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id        UUID NOT NULL,
    subscription_id   UUID NOT NULL,
    price             NUMERIC(12,2) NOT NULL,
    status            VARCHAR(30) NOT NULL,
    billing_period    VARCHAR(30) NOT NULL,
    next_payment_date DATE NOT NULL,

    CONSTRAINT fk_profile_subscription_profile
        FOREIGN KEY (profile_id)
            REFERENCES profiles(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_profile_subscription_subscription
        FOREIGN KEY (subscription_id)
            REFERENCES subscriptions(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_profile_subscription
        UNIQUE(profile_id, subscription_id)
);

CREATE TABLE processed_events
(
    event_id     UUID PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS outbox_events
(
    id                    UUID PRIMARY KEY,
    event_name            VARCHAR(100) NOT NULL,
    event_id              UUID         NOT NULL UNIQUE,
    payload               VARCHAR      NOT NULL,
    status                VARCHAR(20)  NOT NULL,
    retry_count           NUMERIC DEFAULT 0,
    created_at            TIMESTAMP    NOT NULL,
    processing_started_at TIMESTAMP,
    sent_at               TIMESTAMP
);

CREATE INDEX idx_outbox_status_created
    ON outbox_events (status, created_at);
