package com.apl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.AuctionTeamPurse;

@Repository
public class AuctionTeamPurseRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuctionTeamPurseRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<AuctionTeamPurse> purseRowMapper = new RowMapper<>() {
		@Override
		public AuctionTeamPurse mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuctionTeamPurse purse = new AuctionTeamPurse();
			purse.setId(rs.getLong("id"));
			purse.setAuctionId(rs.getLong("auction_id"));
			purse.setTeamId(rs.getLong("team_id"));
			purse.setInitialPurse(rs.getDouble("initial_purse"));
			purse.setRemainingPurse(rs.getDouble("remaining_purse"));
			purse.setCreatedAt(rs.getTimestamp("created_at").toInstant());
			purse.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
			return purse;
		}
	};

	// Create a purse for a team in an auction
	public Long save(AuctionTeamPurse purse) {
		String sql = "INSERT INTO auction_team_purse (auction_id, team_id, initial_purse, remaining_purse, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, now(), now()) RETURNING id";
		return jdbcTemplate.queryForObject(sql, Long.class, purse.getAuctionId(), purse.getTeamId(),
				purse.getInitialPurse(), purse.getRemainingPurse());
	}

	// Fetch purse by auction + team
	public Optional<AuctionTeamPurse> findByAuctionAndTeam(Long auctionId, Long teamId) {
		String sql = "SELECT * FROM auction_team_purse WHERE auction_id=? AND team_id=?";
		List<AuctionTeamPurse> purses = jdbcTemplate.query(sql, purseRowMapper, auctionId, teamId);
		return purses.isEmpty() ? Optional.empty() : Optional.of(purses.get(0));
	}

	// Update remaining purse
	public int updateRemainingPurse(Long id, Double remainingPurse) {
		String sql = "UPDATE auction_team_purse SET remaining_purse=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, remainingPurse, id);
	}

	// Fetch all purses for an auction
	public List<AuctionTeamPurse> findByAuction(Long auctionId) {
		String sql = "SELECT * FROM auction_team_purse WHERE auction_id=? ORDER BY team_id";
		return jdbcTemplate.query(sql, purseRowMapper, auctionId);
	}

	// Add inside AuctionTeamPurseRepository
	public int deductPurse(Long auctionId, Long teamId, double amount) {
		String sql = """
				    UPDATE auction_team_purse
				    SET remaining_purse = remaining_purse - ?, updated_at=now()
				    WHERE auction_id=? AND team_id=? AND remaining_purse >= ?
				""";
		return jdbcTemplate.update(sql, amount, auctionId, teamId, amount);
	}

	public Optional<Double> getRemainingPurse(Long auctionId, Long teamId) {
		String sql = "SELECT remaining_purse FROM auction_team_purse WHERE auction_id=? AND team_id=?";
		List<Double> result = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getDouble("remaining_purse"), auctionId,
				teamId);
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

}
