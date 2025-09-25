package com.apl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.AuctionBid;

@Repository
public class AuctionBidRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuctionBidRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<AuctionBid> auctionBidRowMapper = new RowMapper<>() {
		@Override
		public AuctionBid mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuctionBid bid = new AuctionBid();
			bid.setId(rs.getLong("id"));
			bid.setAuctionPlayerId(rs.getLong("auction_player_id"));
			bid.setTeamId(rs.getLong("team_id"));
			bid.setBidAmount(rs.getDouble("bid_amount"));
			bid.setBidTime(rs.getTimestamp("bid_time").toInstant());
			bid.setCreatedAt(rs.getTimestamp("created_at").toInstant());
			return bid;
		}
	};

	// Place a new bid
	public Long save(AuctionBid bid) {
		String sql = "INSERT INTO auction_bids (auction_player_id, team_id, bid_amount, bid_time, created_at) "
				+ "VALUES (?, ?, ?, now(), now()) RETURNING id";
		return jdbcTemplate.queryForObject(sql, Long.class, bid.getAuctionPlayerId(), bid.getTeamId(),
				bid.getBidAmount());
	}

	// Fetch all bids for a specific auction player
	public List<AuctionBid> findByAuctionPlayerId(Long auctionPlayerId) {
		String sql = "SELECT * FROM auction_bids WHERE auction_player_id=? ORDER BY bid_time ASC";
		return jdbcTemplate.query(sql, auctionBidRowMapper, auctionPlayerId);
	}

	// Fetch highest bid for a specific auction player
	public Optional<AuctionBid> findHighestBid(Long auctionPlayerId) {
		String sql = "SELECT * FROM auction_bids WHERE auction_player_id=? ORDER BY bid_amount DESC, bid_time ASC LIMIT 1";
		List<AuctionBid> bids = jdbcTemplate.query(sql, auctionBidRowMapper, auctionPlayerId);
		return bids.isEmpty() ? Optional.empty() : Optional.of(bids.get(0));
	}

	// Fetch bids by team in a particular auction
	public List<AuctionBid> findByTeamId(Long teamId) {
		String sql = "SELECT * FROM auction_bids WHERE team_id=? ORDER BY bid_time ASC";
		return jdbcTemplate.query(sql, auctionBidRowMapper, teamId);
	}
}
