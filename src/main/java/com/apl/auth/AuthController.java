package com.apl.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.jwt.JwtUtil;
import com.apl.repository.model.User;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtUtil jwtUtil;

	public AuthController(AuthService authService, JwtUtil jwtUtil) {
		this.authService = authService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		Optional<User> userOpt = authService.authenticateByEmail(email, password);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			// For now, assign "USER" role; later we can load roles dynamically
			String token = jwtUtil.generateToken(user.getEmail(), "USER");
			return ResponseEntity.ok(token);
		}
		return ResponseEntity.status(401).body("Invalid email or password");
	}
}
