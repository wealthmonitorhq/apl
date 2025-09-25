package com.apl.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.apl.repository.model.Player;

public class PlayerRowMapper implements RowMapper<Player> {
	@Override
	public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
		Player player = new Player();
		player.setId(rs.getLong("id"));
		player.setFullName(rs.getString("full_name"));
		player.setPhotoUrl(rs.getString("photo_url"));
		player.setRole(rs.getString("role"));
		player.setDob(rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null);
		player.setGender(rs.getString("gender"));
		player.setBattingHand(rs.getString("batting_hand"));
		player.setBowlingHand(rs.getString("bowling_hand"));
		player.setNotes(rs.getString("notes"));
		return player;
	}
}
