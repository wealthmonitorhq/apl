package com.apl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.Role;
import com.apl.repository.rowmapper.RoleRowMapper;

@Repository
public class RoleRepository {

	private final JdbcTemplate jdbcTemplate;

	public RoleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Role> findAll() {
		String sql = "SELECT * FROM roles ORDER BY id";
		return jdbcTemplate.query(sql, new RoleRowMapper());
	}

	public Optional<Role> findById(Long id) {
		String sql = "SELECT * FROM roles WHERE id = ?";
		return jdbcTemplate.query(sql, new RoleRowMapper(), id).stream().findFirst();
	}

	public Optional<Role> findByName(String name) {
		String sql = "SELECT * FROM roles WHERE name = ?";
		return jdbcTemplate.query(sql, new RoleRowMapper(), name).stream().findFirst();
	}

	public int save(Role role) {
		String sql = "INSERT INTO roles (name, description) VALUES (?, ?)";
		return jdbcTemplate.update(sql, role.getName(), role.getDescription());
	}

	public int update(Role role) {
		String sql = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
		return jdbcTemplate.update(sql, role.getName(), role.getDescription(), role.getId());
	}

	public int deleteById(Long id) {
		String sql = "DELETE FROM roles WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}
