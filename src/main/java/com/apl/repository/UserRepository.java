package com.apl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apl.repository.model.User;
import com.apl.repository.rowmapper.UserRowMapper;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;

	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// ---------- Read ----------
	public List<User> findAll() {
		String sql = "SELECT * FROM users";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	public Optional<User> findById(Long id) {
		String sql = "SELECT * FROM users WHERE id = ?";
		return jdbcTemplate.query(sql, new UserRowMapper(), id).stream().findFirst();
	}

	public Optional<User> findByEmail(String email) {
		String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
		return jdbcTemplate.query(sql, new UserRowMapper(), email).stream().findFirst();
	}

	// ---------- Write ----------
	public int save(User user) {
		String sql = "INSERT INTO users (username, email, password_hash, full_name, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, ?, now(), now())";
		return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getFullName(),
				user.isActive());
	}

	public int update(User user) {
		String sql = "UPDATE users SET username = ?, email = ?, password_hash = ?, full_name = ?, is_active = ?, updated_at = now() WHERE id = ?";
		return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getFullName(),
				user.isActive(), user.getId());
	}

	public int deleteById(Long id) {
		String sql = "DELETE FROM users WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}
