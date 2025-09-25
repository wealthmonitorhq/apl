package com.apl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.AuctionRetentionRule;

@Repository
public class AuctionRetentionRuleRepository {

	private final JdbcTemplate jdbcTemplate;

	public AuctionRetentionRuleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<AuctionRetentionRule> rowMapper = new RowMapper<>() {
		@Override
		public AuctionRetentionRule mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuctionRetentionRule rule = new AuctionRetentionRule();
			rule.setId(rs.getLong("id"));
			rule.setAuctionId(rs.getLong("auction_id"));
			rule.setMaxRetained(rs.getInt("max_retained"));
			rule.setMaxRetainedForeign(rs.getInt("max_retained_foreign"));
			rule.setTotalPurseDeduction(
					rs.getObject("total_purse_deduction") != null ? rs.getDouble("total_purse_deduction") : null);
			rule.setCreatedAt(rs.getTimestamp("created_at").toInstant());
			rule.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
			return rule;
		}
	};

	// Create retention rule
	public Long save(AuctionRetentionRule rule) {
		String sql = "INSERT INTO auction_retention_rules (auction_id, max_retained, max_retained_foreign, total_purse_deduction, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, now(), now()) RETURNING id";
		return jdbcTemplate.queryForObject(sql, Long.class, rule.getAuctionId(), rule.getMaxRetained(),
				rule.getMaxRetainedForeign(), rule.getTotalPurseDeduction());
	}

	// Fetch by auction
	public Optional<AuctionRetentionRule> findByAuction(Long auctionId) {
		String sql = "SELECT * FROM auction_retention_rules WHERE auction_id=?";
		List<AuctionRetentionRule> rules = jdbcTemplate.query(sql, rowMapper, auctionId);
		return rules.isEmpty() ? Optional.empty() : Optional.of(rules.get(0));
	}

	// Update retention rule
	public int update(AuctionRetentionRule rule) {
		String sql = "UPDATE auction_retention_rules SET max_retained=?, max_retained_foreign=?, total_purse_deduction=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, rule.getMaxRetained(), rule.getMaxRetainedForeign(),
				rule.getTotalPurseDeduction(), rule.getId());
	}
}
