package com.apl.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.apl.repository.model.Team;

public class TeamRowMapper implements RowMapper<Team> {
	@Override
	public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
		Team team = new Team();
		team.setId(rs.getLong("id"));
		team.setName(rs.getString("name"));
		team.setLogoUrl(rs.getString("logo_url"));
		team.setPlace(rs.getString("place"));
		team.setOwner(rs.getString("owner"));
		team.setFoundedYear(rs.getInt("founded_year"));
		team.setWebsite(rs.getString("website"));
		team.setDescription(rs.getString("description"));
		return team;
	}
}