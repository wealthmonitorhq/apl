package com.apl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.UserRole;
import com.apl.repository.rowmapper.UserRoleRowMapper;

@Repository
public class UserRoleRepository {

	private final JdbcTemplate jdbcTemplate;

	public UserRoleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<UserRole> findAll() {
		String sql = "SELECT * FROM user_roles ORDER BY id";
		return jdbcTemplate.query(sql, new UserRoleRowMapper());
	}

	public Optional<UserRole> findById(Long id) {
		String sql = "SELECT * FROM user_roles WHERE id = ?";
		return jdbcTemplate.query(sql, new UserRoleRowMapper(), id).stream().findFirst();
	}

	public List<UserRole> findByUserId(Long userId) {
		String sql = "SELECT * FROM user_roles WHERE user_id = ?";
		return jdbcTemplate.query(sql, new UserRoleRowMapper(), userId);
	}

	public int assignRoleToUser(Long userId, Long roleId) {
		String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
		return jdbcTemplate.update(sql, userId, roleId);
	}

	public int removeRoleFromUser(Long userId, Long roleId) {
		String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
		return jdbcTemplate.update(sql, userId, roleId);
	}

	public int deleteById(Long id) {
		String sql = "DELETE FROM user_roles WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}
