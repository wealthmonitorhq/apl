package com.apl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.Player;
import com.apl.repository.rowmapper.PlayerRowMapper;

@Repository
public class PlayerRepository {

	private final JdbcTemplate jdbcTemplate;

	public PlayerRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Player> findAll() {
		String sql = "SELECT * FROM players";
		return jdbcTemplate.query(sql, new PlayerRowMapper());
	}

	public Optional<Player> findById(Long id) {
		String sql = "SELECT * FROM players WHERE id = ?";
		return jdbcTemplate.query(sql, new PlayerRowMapper(), id).stream().findFirst();
	}

	public int save(Player player) {
		String sql = "INSERT INTO players (full_name, photo_url, role, dob, gender, batting_hand, bowling_hand, notes, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
		return jdbcTemplate.update(sql, player.getFullName(), player.getPhotoUrl(), player.getRole(), player.getDob(),
				player.getGender(), player.getBattingHand(), player.getBowlingHand(), player.getNotes());
	}

	public int update(Player player) {
		String sql = "UPDATE players SET full_name=?, photo_url=?, role=?, dob=?, gender=?, batting_hand=?, bowling_hand=?, notes=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, player.getFullName(), player.getPhotoUrl(), player.getRole(), player.getDob(),
				player.getGender(), player.getBattingHand(), player.getBowlingHand(), player.getNotes(),
				player.getId());
	}

	public int deleteById(Long id) {
		String sql = "DELETE FROM players WHERE id=?";
		return jdbcTemplate.update(sql, id);
	}

	public List<Player> findByTournamentId(Long tournamentId) {
		String sql = "SELECT p.* FROM players p " + "JOIN player_tournaments pt ON p.id = pt.player_id "
				+ "WHERE pt.tournament_id = ?";
		return jdbcTemplate.query(sql, new PlayerRowMapper(), tournamentId);
	}

	public List<Player> findByTeamId(Long teamId) {
		String sql = "SELECT p.* FROM players p " + "JOIN player_teams pt ON p.id = pt.player_id "
				+ "WHERE pt.team_id = ?";
		return jdbcTemplate.query(sql, new PlayerRowMapper(), teamId);
	}

}
