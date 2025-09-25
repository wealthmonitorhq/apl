package com.apl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.Team;
import com.apl.repository.rowmapper.TeamRowMapper;

@Repository
public class TeamRepository {

	private final JdbcTemplate jdbcTemplate;

	public TeamRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Team> findAll() {
		String sql = "SELECT * FROM teams";
		return jdbcTemplate.query(sql, new TeamRowMapper());
	}

	public Optional<Team> findById(Long id) {
		String sql = "SELECT * FROM teams WHERE id = ?";
		return jdbcTemplate.query(sql, new TeamRowMapper(), id).stream().findFirst();
	}

	public int save(Team team) {
		String sql = "INSERT INTO teams (name, logo_url, place, owner, founded_year, website, description, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, now(), now())";
		return jdbcTemplate.update(sql, team.getName(), team.getLogoUrl(), team.getPlace(), team.getOwner(),
				team.getFoundedYear(), team.getWebsite(), team.getDescription());
	}

	public int update(Team team) {
		String sql = "UPDATE teams SET name = ?, logo_url = ?, place = ?, owner = ?, founded_year = ?, website = ?, description = ?, updated_at = now() WHERE id = ?";
		return jdbcTemplate.update(sql, team.getName(), team.getLogoUrl(), team.getPlace(), team.getOwner(),
				team.getFoundedYear(), team.getWebsite(), team.getDescription(), team.getId());
	}

	public int deleteById(Long id) {
		String sql = "DELETE FROM teams WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}
