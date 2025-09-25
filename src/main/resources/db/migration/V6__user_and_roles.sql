-- V6__users_and_roles.sql
-- User management and Role-based access control (RBAC)

BEGIN;

-- ========================
-- roles (reference table)
-- ========================
CREATE TABLE IF NOT EXISTS roles (
    id          SMALLSERIAL PRIMARY KEY,
    code        TEXT NOT NULL UNIQUE,  -- e.g. ADMIN, TEAM_MANAGER, SCORER, FAN
    name        TEXT NOT NULL,         -- human-readable
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- seed roles
INSERT INTO roles (code, name, description)
VALUES ('ADMIN', 'Administrator', 'Full system access'),
       ('TEAM_MANAGER', 'Team Manager', 'Manages teams and auction participation'),
       ('SCORER', 'Scorer/Official', 'Records match data and updates scores'),
       ('FAN', 'Fan / General User', 'Read-only access for stats and updates')
ON CONFLICT (code) DO NOTHING;

-- ========================
-- users
-- ========================
CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    username      TEXT NOT NULL UNIQUE,
    email         TEXT UNIQUE,
    password_hash TEXT NOT NULL,          -- store bcrypt/argon2 hash, never plain text
    full_name     TEXT,
    is_active     BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMPTZ,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- index for faster login lookups
CREATE INDEX IF NOT EXISTS idx_users_username_lower ON users (LOWER(username));

-- ========================
-- user_roles (many-to-many)
-- ========================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id  SMALLINT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, role_id)
);

-- ========================
-- trigger: set updated_at
-- ========================
DROP TRIGGER IF EXISTS trg_users_set_updated_at ON users;
CREATE TRIGGER trg_users_set_updated_at
  BEFORE UPDATE ON users
  FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at();

COMMIT;
