package com.apl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.Auction;

@Repository
public class AuctionRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuctionRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<Auction> auctionRowMapper = new RowMapper<>() {
		@Override
		public Auction mapRow(ResultSet rs, int rowNum) throws SQLException {
			Auction auction = new Auction();
			auction.setId(rs.getLong("id"));
			auction.setTournamentId(rs.getLong("tournament_id"));
			auction.setName(rs.getString("name"));
			auction.setSeason(rs.getString("season"));
			auction.setAuctionDate(rs.getDate("auction_date").toLocalDate());
			auction.setVenue(rs.getString("venue"));
			auction.setStatus(rs.getString("status"));
			auction.setCreatedAt(rs.getTimestamp("created_at").toInstant());
			auction.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
			return auction;
		}
	};

	// Create auction
	public Long save(Auction auction) {
		String sql = "INSERT INTO auctions (tournament_id, name, season, auction_date, venue, status, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, now(), now()) RETURNING id";
		return jdbcTemplate.queryForObject(sql, Long.class, auction.getTournamentId(), auction.getName(),
				auction.getSeason(), java.sql.Date.valueOf(auction.getAuctionDate()), auction.getVenue(),
				auction.getStatus());
	}

	// Get by ID
	public Optional<Auction> findById(Long id) {
		String sql = "SELECT * FROM auctions WHERE id = ?";
		List<Auction> auctions = jdbcTemplate.query(sql, auctionRowMapper, id);
		if (auctions.isEmpty())
			return Optional.empty();
		return Optional.of(auctions.get(0));
	}

	// Get all auctions
	public List<Auction> findAll() {
		String sql = "SELECT * FROM auctions ORDER BY auction_date DESC";
		return jdbcTemplate.query(sql, auctionRowMapper);
	}

	// Update auction
	public int update(Auction auction) {
		String sql = "UPDATE auctions SET tournament_id=?, name=?, season=?, auction_date=?, venue=?, status=?, updated_at=now() "
				+ "WHERE id=?";
		return jdbcTemplate.update(sql, auction.getTournamentId(), auction.getName(), auction.getSeason(),
				java.sql.Date.valueOf(auction.getAuctionDate()), auction.getVenue(), auction.getStatus(),
				auction.getId());
	}

	// Delete auction
	public int deleteById(Long id) {
		String sql = "DELETE FROM auctions WHERE id=?";
		return jdbcTemplate.update(sql, id);
	}

	// Additional helper: find by tournament & season
	public Optional<Auction> findByTournamentAndSeason(Long tournamentId, String season) {
		String sql = "SELECT * FROM auctions WHERE tournament_id=? AND season=?";
		List<Auction> auctions = jdbcTemplate.query(sql, auctionRowMapper, tournamentId, season);
		if (auctions.isEmpty())
			return Optional.empty();
		return Optional.of(auctions.get(0));
	}

	public int updateStatus(Long auctionId, String status) {
		String sql = "UPDATE auctions SET status=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, status, auctionId);
	}

	public List<Auction> findByAuction(Long auctionId) {
		String sql = "SELECT * FROM auctions WHERE id=?";
		return jdbcTemplate.query(sql, auctionRowMapper, auctionId);
	}

	public List<Auction> findByTournamentId(Long tournamentId) {
		String sql = "SELECT * FROM auctions WHERE tournament_id=? ORDER BY auction_date DESC";
		return jdbcTemplate.query(sql, auctionRowMapper, tournamentId);
	}

}
