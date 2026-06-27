CREATE SCHEMA IF NOT EXISTS auth_service;

CREATE TABLE users
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    username      VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(80) NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_status   VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles
(
    user_id UUID REFERENCES users (id) ON DELETE CASCADE,
    role_id INT REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO roles (type)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');


