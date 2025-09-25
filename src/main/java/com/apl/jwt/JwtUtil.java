package com.apl.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = "mysecretkey1234567890mysecretkey1234567890"; // use env var in prod
	private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hour

	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	public String generateToken(String username, String role) {
		return Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return parseClaims(token).getSubject();
	}

	public String extractRole(String token) {
		return parseClaims(token).get("role", String.class);
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
