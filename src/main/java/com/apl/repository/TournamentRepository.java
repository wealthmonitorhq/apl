package com.apl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.Tournament;
import com.apl.repository.rowmapper.TournamentRowMapper;

@Repository
public class TournamentRepository {

	private final JdbcTemplate jdbcTemplate;

	public TournamentRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Tournament> findAll() {
		String sql = "SELECT * FROM tournaments";
		return jdbcTemplate.query(sql, new TournamentRowMapper());
	}

	public Optional<Tournament> findById(Long id) {
		String sql = "SELECT * FROM tournaments WHERE id = ?";
		return jdbcTemplate.query(sql, new TournamentRowMapper(), id).stream().findFirst();
	}

	public int save(Tournament tournament) {
		String sql = "INSERT INTO tournaments (name, logo_url, place, organizer_name, season, start_date, end_date, prize_money, winner_team_id, runnerup_team_id, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
		return jdbcTemplate.update(sql, tournament.getName(), tournament.getLogoUrl(), tournament.getPlace(),
				tournament.getOrganizerName(), tournament.getSeason(), tournament.getStartDate(),
				tournament.getEndDate(), tournament.getPrizeMoney(), tournament.getWinnerTeamId(),
				tournament.getRunnerUpTeamId());
	}

	public int update(Tournament tournament) {
		String sql = "UPDATE tournaments SET name=?, logo_url=?, place=?, organizer_name=?, season=?, start_date=?, end_date=?, prize_money=?, winner_team_id=?, runnerup_team_id=?, updated_at=now() WHERE id=?";
		return jdbcTemplate.update(sql, tournament.getName(), tournament.getLogoUrl(), tournament.getPlace(),
				tournament.getOrganizerName(), tournament.getSeason(), tournament.getStartDate(),
				tournament.getEndDate(), tournament.getPrizeMoney(), tournament.getWinnerTeamId(),
				tournament.getRunnerUpTeamId(), tournament.getId());
	}

	public int deleteById(Long id) {
		String sql = "DELETE FROM tournaments WHERE id=?";
		return jdbcTemplate.update(sql, id);
	}
}
