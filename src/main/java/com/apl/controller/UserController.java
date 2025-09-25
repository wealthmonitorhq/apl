package com.apl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.repository.model.User;
import com.apl.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// List all users
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	// Get user by id
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.getUserById(id);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user) {
		// Basic validation
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			return ResponseEntity.badRequest().body("Email is required");
		}
		if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
			return ResponseEntity.badRequest().body("Password is required");
		}

		// Set defaults
		user.setActive(true); // user is active by default

		// Password hashing handled in service
		User created = userService.createUser(user);

		return ResponseEntity.ok(created);
	}

	// Delete user
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		if (userService.deleteUser(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
