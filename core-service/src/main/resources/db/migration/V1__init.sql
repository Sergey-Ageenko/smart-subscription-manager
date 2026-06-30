CREATE SCHEMA IF NOT EXISTS core_service;

CREATE TABLE profiles
(
    id         UUID PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(80) NOT NULL

);

CREATE TABLE budgets
(
    id            UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    monthly_limit NUMERIC(10, 2) NOT NULL check (monthly_limit >= 0),
    profile_id    UUID           NOT NULL UNIQUE,

    CONSTRAINT fk_budget_profile
        FOREIGN KEY (profile_id)
            REFERENCES profiles (id)
            ON DELETE CASCADE

);

CREATE TABLE subscriptions
(
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name              VARCHAR(100)   NOT NULL UNIQUE ,
    price             NUMERIC(10, 2) NOT NULL,
    category          VARCHAR(50)    NOT NULL,
    billing_period    VARCHAR(30)    NOT NULL,
    status            VARCHAR(30)    NOT NULL,
    next_payment_date DATE           NOT NULL,
    profile_id            UUID           NOT NULL,

    CONSTRAINT fk_subscription_profile
        FOREIGN KEY (profile_id)
            REFERENCES profiles (id)
            ON DELETE CASCADE
);
