-- V2__match_tables.sql
-- Adds match-level tables: matches, match_innings, player_match_performance

BEGIN;

-- ========================
-- matches
-- ========================
CREATE TABLE IF NOT EXISTS matches (
  id               BIGSERIAL PRIMARY KEY,
  tournament_id    BIGINT REFERENCES tournaments(id) ON DELETE SET NULL,
  format_id        SMALLINT REFERENCES match_formats(id) ON DELETE SET NULL,
  venue            TEXT,
  city             TEXT,
  country          TEXT,
  start_date       DATE NOT NULL,
  end_date         DATE,
  team1_id         BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  team2_id         BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  toss_winner_id   BIGINT REFERENCES teams(id) ON DELETE SET NULL,
  toss_decision    TEXT CHECK (toss_decision IN ('bat','bowl')),
  winner_team_id   BIGINT REFERENCES teams(id) ON DELETE SET NULL,
  result           TEXT,                  -- e.g. "won by 5 wickets", "draw", "no result"
  overs_per_side   SMALLINT,              -- e.g. 20 for T20, 50 for ODI
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_match_dates CHECK (end_date IS NULL OR start_date <= end_date)
);

CREATE INDEX IF NOT EXISTS idx_matches_tournament ON matches (tournament_id);
CREATE INDEX IF NOT EXISTS idx_matches_date ON matches (start_date);

-- ========================
-- match_innings
-- ========================
CREATE TABLE IF NOT EXISTS match_innings (
  id               BIGSERIAL PRIMARY KEY,
  match_id         BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  innings_number   SMALLINT NOT NULL CHECK (innings_number >= 1),
  batting_team_id  BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  bowling_team_id  BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  runs_scored      INTEGER NOT NULL DEFAULT 0 CHECK (runs_scored >= 0),
  wickets_lost     SMALLINT NOT NULL DEFAULT 0 CHECK (wickets_lost >= 0),
  overs_faced      NUMERIC(5,1),         -- allows partial overs (e.g., 19.5 overs)
  declared         BOOLEAN NOT NULL DEFAULT FALSE,
  follow_on        BOOLEAN NOT NULL DEFAULT FALSE,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_match_innings UNIQUE (match_id, innings_number)
);

CREATE INDEX IF NOT EXISTS idx_match_innings_match ON match_innings (match_id);

-- ========================
-- player_match_performance
-- ========================
CREATE TABLE IF NOT EXISTS player_match_performance (
  id               BIGSERIAL PRIMARY KEY,
  match_id         BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  player_id        BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
  team_id          BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  innings_number   SMALLINT NOT NULL,    -- which innings this performance belongs to

  -- Batting stats
  batting_runs     INTEGER CHECK (batting_runs >= 0),
  batting_balls    INTEGER CHECK (batting_balls >= 0),
  batting_fours    SMALLINT DEFAULT 0,
  batting_sixes    SMALLINT DEFAULT 0,
  dismissal_mode   TEXT,                 -- e.g. "bowled", "caught", "not out"
  dismissal_by_id  BIGINT REFERENCES players(id) ON DELETE SET NULL,
  batting_position SMALLINT,

  -- Bowling stats
  bowling_overs    NUMERIC(5,1),         
  bowling_maidens  SMALLINT DEFAULT 0,
  bowling_runs     INTEGER CHECK (bowling_runs >= 0),
  bowling_wickets  SMALLINT DEFAULT 0,
  no_balls         SMALLINT DEFAULT 0,
  wides            SMALLINT DEFAULT 0,

  -- Fielding
  catches          SMALLINT DEFAULT 0,
  stumpings        SMALLINT DEFAULT 0,
  runouts          SMALLINT DEFAULT 0,

  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT uq_player_match UNIQUE (match_id, player_id, innings_number)
);

CREATE INDEX IF NOT EXISTS idx_player_match_performance_match ON player_match_performance (match_id);
CREATE INDEX IF NOT EXISTS idx_player_match_performance_player ON player_match_performance (player_id);

-- ========================
-- Triggers for updated_at
-- ========================
DROP TRIGGER IF EXISTS trg_matches_set_updated_at ON matches;
CREATE TRIGGER trg_matches_set_updated_at
  BEFORE UPDATE ON matches
  FOR EACH ROW EXECUTE PROCEDURE set_updated_at();

DROP TRIGGER IF EXISTS trg_match_innings_set_updated_at ON match_innings;
CREATE TRIGGER trg_match_innings_set_updated_at
  BEFORE UPDATE ON match_innings
  FOR EACH ROW EXECUTE PROCEDURE set_updated_at();

DROP TRIGGER IF EXISTS trg_player_match_performance_set_updated_at ON player_match_performance;
CREATE TRIGGER trg_player_match_performance_set_updated_at
  BEFORE UPDATE ON player_match_performance
  FOR EACH ROW EXECUTE PROCEDURE set_updated_at();

COMMIT;
