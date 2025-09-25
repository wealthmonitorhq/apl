package com.apl.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.apl.repository.model.Tournament;

public class TournamentRowMapper implements RowMapper<Tournament> {
	@Override
	public Tournament mapRow(ResultSet rs, int rowNum) throws SQLException {
		Tournament t = new Tournament();
		t.setId(rs.getLong("id"));
		t.setName(rs.getString("name"));
		t.setLogoUrl(rs.getString("logo_url"));
		t.setPlace(rs.getString("place"));
		t.setOrganizerName(rs.getString("organizer_name"));
		t.setSeason(rs.getString("season"));
		t.setStartDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null);
		t.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
		t.setPrizeMoney(rs.getBigDecimal("prize_money"));
		t.setWinnerTeamId(rs.getLong("winner_team_id"));
		t.setRunnerUpTeamId(rs.getLong("runnerup_team_id"));
		return t;
	}
}
