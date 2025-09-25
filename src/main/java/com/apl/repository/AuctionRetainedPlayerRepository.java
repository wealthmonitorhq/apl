package com.apl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.AuctionRetainedPlayer;

@Repository
public class AuctionRetainedPlayerRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuctionRetainedPlayerRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<AuctionRetainedPlayer> rowMapper = new RowMapper<>() {
		@Override
		public AuctionRetainedPlayer mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuctionRetainedPlayer player = new AuctionRetainedPlayer();
			player.setId(rs.getLong("id"));
			player.setAuctionId(rs.getLong("auction_id"));
			player.setTeamId(rs.getLong("team_id"));
			player.setPlayerId(rs.getLong("player_id"));
			player.setRetentionPrice(rs.getDouble("retention_price"));
			player.setIsForeign(rs.getBoolean("is_foreign"));
			player.setCreatedAt(rs.getTimestamp("created_at").toInstant());
			player.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
			return player;
		}
	};

	// Add retained player
	public Long save(AuctionRetainedPlayer player) {
		String sql = "INSERT INTO auction_retained_players (auction_id, team_id, player_id, retention_price, is_foreign, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, now(), now()) RETURNING id";
		return jdbcTemplate.queryForObject(sql, Long.class, player.getAuctionId(), player.getTeamId(),
				player.getPlayerId(), player.getRetentionPrice(), player.getIsForeign());
	}

	// Fetch all retained players for an auction
	public List<AuctionRetainedPlayer> findByAuction(Long auctionId) {
		String sql = "SELECT * FROM auction_retained_players WHERE auction_id=? ORDER BY team_id, player_id";
		return jdbcTemplate.query(sql, rowMapper, auctionId);
	}

	// Fetch retained players for a team
	public List<AuctionRetainedPlayer> findByAuctionAndTeam(Long auctionId, Long teamId) {
		String sql = "SELECT * FROM auction_retained_players WHERE auction_id=? AND team_id=? ORDER BY player_id";
		return jdbcTemplate.query(sql, rowMapper, auctionId, teamId);
	}

	// Delete retained player (e.g., if auction rules change)
	public int delete(Long id) {
		String sql = "DELETE FROM auction_retained_players WHERE id=?";
		return jdbcTemplate.update(sql, id);
	}
}
