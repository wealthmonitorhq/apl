-- V3__ball_by_ball.sql
-- Adds ball-by-ball granularity table for matches

BEGIN;

CREATE TABLE IF NOT EXISTS ball_by_ball (
  id               BIGSERIAL PRIMARY KEY,
  match_id         BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  innings_id       BIGINT NOT NULL REFERENCES match_innings(id) ON DELETE CASCADE,
  over_number      SMALLINT NOT NULL CHECK (over_number >= 1),
  ball_in_over     SMALLINT NOT NULL CHECK (ball_in_over BETWEEN 1 AND 30),
  
  -- participants
  striker_id       BIGINT NOT NULL REFERENCES players(id) ON DELETE RESTRICT,
  non_striker_id   BIGINT REFERENCES players(id) ON DELETE SET NULL,
  bowler_id        BIGINT NOT NULL REFERENCES players(id) ON DELETE RESTRICT,
  batting_team_id  BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  bowling_team_id  BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  
  -- outcomes
  runs_batsman     SMALLINT NOT NULL DEFAULT 0 CHECK (runs_batsman >= 0),
  runs_extras      SMALLINT NOT NULL DEFAULT 0 CHECK (runs_extras >= 0),
  extra_type       TEXT CHECK (extra_type IN ('wide','no-ball','bye','leg-bye','penalty')),
  wicket           BOOLEAN NOT NULL DEFAULT FALSE,
  
  -- dismissal details (if wicket = true)
  dismissal_mode   TEXT,  -- e.g. "caught", "bowled", "run out", "lbw", "stumped"
  dismissed_player_id BIGINT REFERENCES players(id) ON DELETE SET NULL,
  fielder_id       BIGINT REFERENCES players(id) ON DELETE SET NULL, -- catcher/runner
  
  -- commentary feed
  commentary       TEXT,
  
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT uq_ball UNIQUE (innings_id, over_number, ball_in_over)
);

-- indexes
CREATE INDEX IF NOT EXISTS idx_ball_by_ball_match ON ball_by_ball (match_id);
CREATE INDEX IF NOT EXISTS idx_ball_by_ball_innings ON ball_by_ball (innings_id);
CREATE INDEX IF NOT EXISTS idx_ball_by_ball_bowler ON ball_by_ball (bowler_id);
CREATE INDEX IF NOT EXISTS idx_ball_by_ball_batsman ON ball_by_ball (striker_id);

-- trigger to maintain updated_at
DROP TRIGGER IF EXISTS trg_ball_by_ball_set_updated_at ON ball_by_ball;
CREATE TRIGGER trg_ball_by_ball_set_updated_at
  BEFORE UPDATE ON ball_by_ball
  FOR EACH ROW EXECUTE PROCEDURE set_updated_at();

COMMIT;
