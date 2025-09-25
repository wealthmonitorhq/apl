package com.apl.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.apl.repository.model.User;

public class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User u = new User();
		u.setId(rs.getLong("id"));
		u.setUsername(rs.getString("username"));
		u.setEmail(rs.getString("email"));
		u.setPasswordHash(rs.getString("password_hash"));
		u.setFullName(rs.getString("full_name"));
		u.setActive(rs.getBoolean("is_active"));

		Timestamp lastLogin = rs.getTimestamp("last_login_at");
		if (lastLogin != null)
			u.setLastLoginAt(lastLogin.toInstant());

		Timestamp created = rs.getTimestamp("created_at");
		if (created != null)
			u.setCreatedAt(created.toInstant());

		Timestamp updated = rs.getTimestamp("updated_at");
		if (updated != null)
			u.setUpdatedAt(updated.toInstant());

		return u;
	}
}