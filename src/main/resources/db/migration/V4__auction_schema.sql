-- V4__auction_schema.sql
-- Auction schema for cricket management app

BEGIN;

-- ==============================
--  Auctions (per tournament)
-- ==============================
CREATE TABLE IF NOT EXISTS auctions (
    id              BIGSERIAL PRIMARY KEY,
    tournament_id   BIGINT NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
    name            TEXT NOT NULL,
    season          TEXT,
    auction_date    DATE NOT NULL,
    venue           TEXT,
    status          TEXT CHECK (status IN ('upcoming','live','completed')) DEFAULT 'upcoming',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_auction_tournament UNIQUE (tournament_id, season)
);

-- ==============================
--  Auction Players
-- ==============================
CREATE TABLE IF NOT EXISTS auction_players (
    id              BIGSERIAL PRIMARY KEY,
    auction_id      BIGINT NOT NULL REFERENCES auctions(id) ON DELETE CASCADE,
    player_id       BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    base_price      NUMERIC(14,2) NOT NULL CHECK (base_price >= 0),
    final_price     NUMERIC(14,2) CHECK (final_price >= 0),
    status          TEXT CHECK (status IN ('unsold','sold','withdrawn')) DEFAULT 'unsold',
    sold_team_id    BIGINT REFERENCES teams(id) ON DELETE SET NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_player_auction UNIQUE (auction_id, player_id)
);

-- ==============================
--  Auction Bids
-- ==============================
CREATE TABLE IF NOT EXISTS auction_bids (
    id                BIGSERIAL PRIMARY KEY,
    auction_player_id BIGINT NOT NULL REFERENCES auction_players(id) ON DELETE CASCADE,
    team_id           BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    bid_amount        NUMERIC(14,2) NOT NULL CHECK (bid_amount > 0),
    bid_time          TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_bids_player ON auction_bids (auction_player_id);
CREATE INDEX IF NOT EXISTS idx_bids_team ON auction_bids (team_id);

-- ==============================
--  Auction Team Purse
-- ==============================
CREATE TABLE IF NOT EXISTS auction_team_purse (
    id              BIGSERIAL PRIMARY KEY,
    auction_id      BIGINT NOT NULL REFERENCES auctions(id) ON DELETE CASCADE,
    team_id         BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    initial_purse   NUMERIC(14,2) NOT NULL CHECK (initial_purse >= 0),
    remaining_purse NUMERIC(14,2) NOT NULL CHECK (remaining_purse >= 0),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_auction_team UNIQUE (auction_id, team_id)
);

COMMIT;
