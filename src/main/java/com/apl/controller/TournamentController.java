package com.apl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.repository.model.Auction;
import com.apl.repository.model.Tournament;
import com.apl.service.TournamentService;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

	private final TournamentService service;

	public TournamentController(TournamentService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Tournament>> getAllTournaments() {
		return ResponseEntity.ok(service.getAllTournaments());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
		return service.getTournamentById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
		return ResponseEntity.ok(service.createTournament(tournament));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
		tournament.setId(id);
		return ResponseEntity.ok(service.updateTournament(tournament));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
		if (service.deleteTournament(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	// ----------------- Auctions for Tournament -----------------
	@GetMapping("/{tournamentId}/auctions")
	public ResponseEntity<List<Auction>> getAuctionsForTournament(@PathVariable Long tournamentId) {
		return ResponseEntity.ok(service.getAuctionsForTournament(tournamentId));
	}
}
