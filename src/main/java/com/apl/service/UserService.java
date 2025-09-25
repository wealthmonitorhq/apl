package com.apl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apl.repository.UserRepository;
import com.apl.repository.model.User;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserRoleService userRoleService; // used for assigning roles
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, UserRoleService userRoleService) {
		this.userRepository = userRepository;
		this.userRoleService = userRoleService;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	/**
	 * Create a user with password hashing and default role assignment.
	 */
	public User createUser(User user) {
		// 1. Hash the password
		String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
		user.setPasswordHash(hashedPassword);

		// 2. Save user to DB
		userRepository.save(user);

		// 3. Assign default "USER" role
		// Assuming the role with ID 1 is USER, or you fetch it from DB
		Long defaultUserRoleId = 1L;
		userRoleService.assignRole(user.getId(), defaultUserRoleId);

		return user;
	}

	public boolean deleteUser(Long id) {
		return userRepository.deleteById(id) > 0;
	}
}
