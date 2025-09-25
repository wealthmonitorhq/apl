package com.apl.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apl.repository.UserRepository;
import com.apl.repository.model.User;

import java.util.Optional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Authenticate user by email and password. Only active users can log in.
	 *
	 * @param email       Email of the user
	 * @param rawPassword Plaintext password entered by user
	 * @return Optional<User> if authentication succeeds, else Optional.empty()
	 */
	public Optional<User> authenticateByEmail(String email, String rawPassword) {
		Optional<User> userOpt = userRepository.findByEmail(email);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			// Reject login if user is inactive
			if (!user.isActive()) {
				return Optional.empty();
			}

			// Check password
			if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
				return Optional.of(user);
			}
		}

		return Optional.empty();
	}
}
