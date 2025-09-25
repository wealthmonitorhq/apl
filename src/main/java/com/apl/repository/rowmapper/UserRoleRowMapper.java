package com.apl.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.apl.repository.model.UserRole;

public class UserRoleRowMapper implements RowMapper<UserRole> {
	@Override
	public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserRole ur = new UserRole();
		ur.setId(rs.getLong("id"));
		ur.setUserId(rs.getLong("user_id"));
		ur.setRoleId(rs.getLong("role_id"));
		return ur;
	}
}