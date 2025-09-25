package com.apl.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.apl.repository.model.Role;

public class RoleRowMapper implements RowMapper<Role> {
	@Override
	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		Role role = new Role();
		role.setId(rs.getLong("id"));
		role.setName(rs.getString("name"));
		role.setDescription(rs.getString("description"));
		return role;
	}
}
