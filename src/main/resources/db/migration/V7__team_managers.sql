-- V7__team_managers.sql
-- Link Team Managers (users) to Teams

BEGIN;

CREATE TABLE IF NOT EXISTS team_managers (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    team_id     BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    is_primary  BOOLEAN NOT NULL DEFAULT FALSE, -- one main manager, rest assistants
    CONSTRAINT uq_team_manager UNIQUE (user_id, team_id)
);

CREATE INDEX IF NOT EXISTS idx_team_managers_team ON team_managers (team_id);
CREATE INDEX IF NOT EXISTS idx_team_managers_user ON team_managers (user_id);

COMMIT;
