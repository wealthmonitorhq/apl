package com.apl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.repository.model.Player;
import com.apl.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService service;

	public PlayerController(PlayerService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Player>> getAllPlayers() {
		return ResponseEntity.ok(service.getAllPlayers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
		return service.getPlayerById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
		return ResponseEntity.ok(service.createPlayer(player));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
		player.setId(id);
		return ResponseEntity.ok(service.updatePlayer(player));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
		if (service.deletePlayer(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
