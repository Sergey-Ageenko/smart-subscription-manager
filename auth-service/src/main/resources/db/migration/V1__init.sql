CREATE SCHEMA IF NOT EXISTS auth_service;

CREATE TABLE IF NOT EXISTS users
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    username      VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(80) NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_status   VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS roles
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id UUID REFERENCES users (id) ON DELETE CASCADE,
    role_id INT REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS outbox_events
(
    id           UUID PRIMARY KEY,
    event_name   VARCHAR(100) NOT NULL,
    payload      VARCHAR      NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    sent_at      TIMESTAMP
);

CREATE INDEX idx_outbox_status_created
    ON outbox_events(status, created_at);

CREATE INDEX idx_role_id
    ON user_roles(role_id);

INSERT INTO roles (type)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');


