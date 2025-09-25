package com.apl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.AuctionPlayer;

@Repository
public class AuctionPlayerRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuctionPlayerRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<AuctionPlayer> auctionPlayerRowMapper = new RowMapper<>() {
		@Override
		public AuctionPlayer mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuctionPlayer player = new AuctionPlayer();
			player.setId(rs.getLong("id"));
			player.setAuctionId(rs.getLong("auction_id"));
			player.setPlayerId(rs.getLong("player_id"));
			player.setBasePrice(rs.getDouble("base_price"));
			player.setFinalPrice(rs.getObject("final_price") != null ? rs.getDouble("final_price") : null);
			player.setStatus(rs.getString("status"));
			player.setSoldTeamId(rs.getObject("sold_team_id") != null ? rs.getLong("sold_team_id") : null);
			player.setCreatedAt(rs.getTimestamp("created_at").toInstant());
			player.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
			return player;
		}
	};

	public int save(AuctionPlayer player) {
		// Check if player is registered for the tournament
		String checkSql = "SELECT COUNT(*) FROM tournament_players WHERE tournament_id=? AND player_id=?";
		Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, player.getTournamentId(),
				player.getPlayerId());
		if (count == null || count == 0) {
			throw new IllegalStateException("Player not registered for this tournament!");
		}

		String sql = "INSERT INTO auction_players (auction_id, player_id, base_price, status, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, now(), now())";
		return jdbcTemplate.update(sql, player.getAuctionId(), player.getPlayerId(), player.getBasePrice(),
				player.getStatus());
	}

	public List<AuctionPlayer> findByAuctionId(Long auctionId) {
		String sql = "SELECT * FROM auction_players WHERE auction_id=?";
		return jdbcTemplate.query(sql, auctionPlayerRowMapper, auctionId);
	}

	// Fetch by ID
	public Optional<AuctionPlayer> findById(Long id) {
		String sql = "SELECT * FROM auction_players WHERE id=?";
		List<AuctionPlayer> players = jdbcTemplate.query(sql, auctionPlayerRowMapper, id);
		return players.isEmpty() ? Optional.empty() : Optional.of(players.get(0));
	}

	// Update status, final price, and sold team
	public int markSold(Long auctionPlayerId, Long soldTeamId, Double finalPrice) {
		String sql = "UPDATE auction_players SET status='sold', sold_team_id=?, final_price=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, soldTeamId, finalPrice, auctionPlayerId);
	}

	public int markWithdrawn(Long auctionPlayerId) {
		String sql = "UPDATE auction_players SET status='withdrawn', updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, auctionPlayerId);
	}

	// Update base price (if needed)
	public int updateBasePrice(Long auctionPlayerId, Double basePrice) {
		String sql = "UPDATE auction_players SET base_price=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, basePrice, auctionPlayerId);
	}

	// Add inside AuctionPlayerRepository
	public int markPlayerSold(Long auctionPlayerId, Long teamId, double finalPrice) {
		String sql = "UPDATE auction_players SET status='sold', sold_team_id=?, final_price=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, teamId, finalPrice, auctionPlayerId);
	}

	public Optional<AuctionPlayer> findByAuctionPlayer(Long auctionPlayerId) {
		String sql = "SELECT * FROM auction_players WHERE id=?";
		List<AuctionPlayer> players = jdbcTemplate.query(sql, auctionPlayerRowMapper, auctionPlayerId);
		return players.isEmpty() ? Optional.empty() : Optional.of(players.get(0));
	}

}
