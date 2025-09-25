-- V5__auction_retention.sql
-- Retention rules and retained players for auctions

BEGIN;

-- ==============================
--  Auction Retention Rules
-- ==============================
CREATE TABLE IF NOT EXISTS auction_retention_rules (
    id              BIGSERIAL PRIMARY KEY,
    auction_id      BIGINT NOT NULL REFERENCES auctions(id) ON DELETE CASCADE,
    max_retained    INT NOT NULL CHECK (max_retained >= 0),        -- e.g. 4 players max
    max_retained_foreign INT NOT NULL CHECK (max_retained_foreign >= 0), -- e.g. 2 foreign players
    total_purse_deduction NUMERIC(14,2) CHECK (total_purse_deduction >= 0), -- if league enforces fixed deduction
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_retention_auction UNIQUE (auction_id)
);

-- ==============================
--  Retained Players
-- ==============================
CREATE TABLE IF NOT EXISTS auction_retained_players (
    id              BIGSERIAL PRIMARY KEY,
    auction_id      BIGINT NOT NULL REFERENCES auctions(id) ON DELETE CASCADE,
    team_id         BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    player_id       BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    retention_price NUMERIC(14,2) NOT NULL CHECK (retention_price >= 0),
    is_foreign      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_retained_player UNIQUE (auction_id, player_id)
);

COMMIT;
