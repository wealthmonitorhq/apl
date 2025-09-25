-- V1__initial_schema.sql
-- Initial schema for Cricket Management App
-- PostgreSQL

BEGIN;

-- ========================
-- ENUMs / helper types
-- ========================
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'gender_type') THEN
    CREATE TYPE gender_type AS ENUM ('male','female','other','unknown');
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'player_role') THEN
    CREATE TYPE player_role AS ENUM ('batsman','bowler','battingallrounder','bowlingallrounder','wicketkeeper','coach','other');
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'handedness') THEN
    CREATE TYPE handedness AS ENUM ('right','left','unknown');
  END IF;
END$$;

-- ========================
-- Lookup: match_formats (extensible)
-- ========================
CREATE TABLE IF NOT EXISTS match_formats (
  id               SMALLSERIAL PRIMARY KEY,
  code             TEXT NOT NULL UNIQUE, -- e.g. TEST, ODI, T20, FC, LA
  name             TEXT NOT NULL,
  description      TEXT,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Seed some common formats (optional - run separately if you prefer)
INSERT INTO match_formats (code, name)
  VALUES ('TEST','Test')
       , ('ODI','One Day International')
       , ('T20','Twenty20')
       , ('T10','Ten10')
       , ('E8','Eight8')
  ON CONFLICT (code) DO NOTHING;

-- ========================
-- players
-- ========================
CREATE TABLE IF NOT EXISTS players (
  id               BIGSERIAL PRIMARY KEY,
  full_name        TEXT NOT NULL,
  photo_url        TEXT,                 -- store externally (S3/CDN) and save URL here
  photo_meta       JSONB,                -- optional (size, alt text etc)
  dob              DATE,
  gender           gender_type NOT NULL DEFAULT 'unknown',
  role             player_role,
  batting_hand     handedness DEFAULT 'unknown',
  bowling_hand     handedness DEFAULT 'unknown',
  notes            TEXT,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_dob_past CHECK (dob IS NULL OR dob <= CURRENT_DATE)
);

-- functional index for case-insensitive name searches
CREATE INDEX IF NOT EXISTS idx_players_full_name_lower ON players (LOWER(full_name));

-- ========================
-- teams
-- ========================
CREATE TABLE IF NOT EXISTS teams (
  id               BIGSERIAL PRIMARY KEY,
  name             TEXT NOT NULL,
  logo_url         TEXT,
  place            TEXT,
  owner            TEXT,
  founded_year     SMALLINT CHECK (founded_year IS NULL OR founded_year > 1800),
  website          TEXT,
  description      TEXT,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Add unique constraint for teams name with DO block
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint 
    WHERE conname = 'teams_name_unique' 
    AND conrelid = 'teams'::regclass
  ) THEN
    ALTER TABLE teams ADD CONSTRAINT teams_name_unique UNIQUE (name);
  END IF;
END$$;

CREATE INDEX IF NOT EXISTS idx_teams_name_lower ON teams (LOWER(name));

-- ========================
-- tournaments
-- ========================
CREATE TABLE IF NOT EXISTS tournaments (
  id               BIGSERIAL PRIMARY KEY,
  name             TEXT NOT NULL,
  logo_url         TEXT,
  place            TEXT,
  organizer_name   TEXT,
  season           TEXT,                 -- e.g. 2024/25, 2025, 2025-India
  start_date       DATE,
  end_date         DATE,
  prize_money      NUMERIC(14,2) DEFAULT 0 CHECK (prize_money >= 0),
  winner_team_id   BIGINT REFERENCES teams(id) ON DELETE SET NULL,
  runnerup_team_id BIGINT REFERENCES teams(id) ON DELETE SET NULL,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_tour_dates CHECK (start_date IS NULL OR end_date IS NULL OR start_date <= end_date)
);

-- Add unique constraint for tournaments name+season with DO block
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint 
    WHERE conname = 'tournaments_name_season_unique' 
    AND conrelid = 'tournaments'::regclass
  ) THEN
    ALTER TABLE tournaments ADD CONSTRAINT tournaments_name_season_unique UNIQUE (name, season);
  END IF;
END$$;

-- ========================
-- player_teams (many-to-many + history)
-- ========================
CREATE TABLE IF NOT EXISTS player_teams (
  id               BIGSERIAL PRIMARY KEY,
  player_id        BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
  team_id          BIGINT NOT NULL REFERENCES teams(id) ON DELETE RESTRICT,
  role             TEXT,                 -- e.g. captain, player, coach (free text for flexibility)
  start_date       DATE,
  end_date         DATE,
  is_current       BOOLEAN NOT NULL DEFAULT TRUE,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_player_team_dates CHECK (start_date IS NULL OR end_date IS NULL OR start_date <= end_date)
);

CREATE INDEX IF NOT EXISTS idx_player_teams_player ON player_teams (player_id);
CREATE INDEX IF NOT EXISTS idx_player_teams_team ON player_teams (team_id);

-- ========================
-- player_batting_stats (aggregated career stats)
-- normalized by (player, format, team)
-- ========================
CREATE TABLE IF NOT EXISTS player_batting_stats (
  id               BIGSERIAL PRIMARY KEY,
  player_id        BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
  format_id        SMALLINT REFERENCES match_formats(id) ON DELETE SET NULL, -- NULL = overall
  team_id          BIGINT REFERENCES teams(id) ON DELETE SET NULL,          -- NULL = overall / domestic career
  matches          INTEGER NOT NULL DEFAULT 0 CHECK (matches >= 0),
  innings          INTEGER NOT NULL DEFAULT 0 CHECK (innings >= 0),
  runs             INTEGER NOT NULL DEFAULT 0 CHECK (runs >= 0),
  balls            INTEGER NOT NULL DEFAULT 0 CHECK (balls >= 0),
  highest          INTEGER,            -- highest score
  average          NUMERIC(7,2),       -- nullable (compute as runs / dismissals)
  strike_rate      NUMERIC(7,2),
  not_out          INTEGER NOT NULL DEFAULT 0 CHECK (not_out >= 0),
  fours            INTEGER NOT NULL DEFAULT 0 CHECK (fours >= 0),
  sixes            INTEGER NOT NULL DEFAULT 0 CHECK (sixes >= 0),
  ducks            INTEGER NOT NULL DEFAULT 0 CHECK (ducks >= 0),
  fifties          INTEGER NOT NULL DEFAULT 0 CHECK (fifties >= 0),
  hundreds         INTEGER NOT NULL DEFAULT 0 CHECK (hundreds >= 0),
  last_updated     TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_batting_unique UNIQUE (player_id, format_id, team_id)
);

CREATE INDEX IF NOT EXISTS idx_batting_player_format ON player_batting_stats (player_id, format_id);

-- ========================
-- player_bowling_stats (aggregated career stats)
-- normalized by (player, format, team)
-- ========================
CREATE TABLE IF NOT EXISTS player_bowling_stats (
  id               BIGSERIAL PRIMARY KEY,
  player_id        BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
  format_id        SMALLINT REFERENCES match_formats(id) ON DELETE SET NULL,
  team_id          BIGINT REFERENCES teams(id) ON DELETE SET NULL,
  balls            INTEGER NOT NULL DEFAULT 0 CHECK (balls >= 0),
  runs_conceded    INTEGER NOT NULL DEFAULT 0 CHECK (runs_conceded >= 0),
  maidens          INTEGER NOT NULL DEFAULT 0 CHECK (maidens >= 0),
  wickets          INTEGER NOT NULL DEFAULT 0 CHECK (wickets >= 0),
  average          NUMERIC(7,2),       -- runs_per_wicket, nullable if wickets = 0
  economy          NUMERIC(7,3),
  strike_rate      NUMERIC(7,2),
  bbi_wickets      SMALLINT,           -- best bowling in an innings (wickets)
  bbi_runs         SMALLINT,           -- best bowling in an innings (runs)
  bbm_wickets      SMALLINT,           -- best bowling in match wickets
  bbm_runs         SMALLINT,
  three_w          INTEGER NOT NULL DEFAULT 0, -- number of 3-wicket innings
  four_w           INTEGER NOT NULL DEFAULT 0,
  five_w           INTEGER NOT NULL DEFAULT 0,
  ten_w            INTEGER NOT NULL DEFAULT 0,
  last_updated     TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_bowling_unique UNIQUE (player_id, format_id, team_id)
);

CREATE INDEX IF NOT EXISTS idx_bowling_player_format ON player_bowling_stats (player_id, format_id);

-- ========================
-- TRIGGER to keep updated_at current
-- ========================
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add trigger to tables that have updated_at (using EXECUTE FUNCTION for modern PostgreSQL)
DROP TRIGGER IF EXISTS trg_players_set_updated_at ON players;
CREATE TRIGGER trg_players_set_updated_at
  BEFORE UPDATE ON players
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

DROP TRIGGER IF EXISTS trg_teams_set_updated_at ON teams;
CREATE TRIGGER trg_teams_set_updated_at
  BEFORE UPDATE ON teams
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

DROP TRIGGER IF EXISTS trg_tournaments_set_updated_at ON tournaments;
CREATE TRIGGER trg_tournaments_set_updated_at
  BEFORE UPDATE ON tournaments
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

DROP TRIGGER IF EXISTS trg_player_teams_set_updated_at ON player_teams;
CREATE TRIGGER trg_player_teams_set_updated_at
  BEFORE UPDATE ON player_teams
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Note: batting & bowling stats have last_updated; update them via app/service logic when aggregates change.

COMMIT;