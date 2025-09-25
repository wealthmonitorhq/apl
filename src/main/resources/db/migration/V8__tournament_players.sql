-- V8__tournament_players.sql

BEGIN;

CREATE TABLE IF NOT EXISTS tournament_players (
    id BIGSERIAL PRIMARY KEY,
    tournament_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    role VARCHAR(50),
    joined_at TIMESTAMP DEFAULT now(),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (player_id) REFERENCES players(id)
);

COMMIT;