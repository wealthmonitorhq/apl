package com.apl.controller;

import com.apl.repository.model.*;
import com.apl.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

	private final AuctionService auctionService;

	public AuctionController(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	// ===============================
	// Auction lifecycle
	// ===============================

	@PostMapping
	public ResponseEntity<Long> createAuction(@RequestBody Auction auction) {
		return ResponseEntity.ok(auctionService.createAuction(auction));
	}

	@GetMapping("/{auctionId}")
	public ResponseEntity<Auction> getAuction(@PathVariable Long auctionId) {
		Optional<Auction> auction = auctionService.getAuction(auctionId);
		return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/tournament/{tournamentId}")
	public ResponseEntity<List<Auction>> getTournamentsAuctions(@PathVariable Long tournamentId) {
		return ResponseEntity.ok(auctionService.getTournamentsAuctions(tournamentId));
	}

	@PutMapping("/{auctionId}/status")
	public ResponseEntity<Void> updateAuctionStatus(@PathVariable Long auctionId, @RequestParam String status) {
		int updated = auctionService.updateAuctionStatus(auctionId, status);
		return updated > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

	// ===============================
	// Auction Players
	// ===============================

	@PostMapping("/{auctionId}/players")
	public ResponseEntity<Long> registerPlayerForAuction(@PathVariable Long auctionId,
			@RequestBody AuctionPlayer player) {
		player.setAuctionId(auctionId);
		return ResponseEntity.ok(auctionService.registerPlayerForAuction(player));
	}

	@GetMapping("/{auctionId}/players")
	public ResponseEntity<List<AuctionPlayer>> getAuctionPlayers(@PathVariable Long auctionId) {
		return ResponseEntity.ok(auctionService.getAuctionPlayers(auctionId));
	}

	@PostMapping("/players/{auctionPlayerId}/sell")
	public ResponseEntity<?> markPlayerSold(@PathVariable Long auctionPlayerId, @RequestParam Long teamId,
			@RequestParam double finalPrice) {
		try {
			boolean success = auctionService.markPlayerSold(auctionPlayerId, teamId, finalPrice);
			return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Cannot mark player sold");
		} catch (IllegalStateException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	// ===============================
	// Bidding
	// ===============================

	@PostMapping("/bids")
	public ResponseEntity<?> placeBid(@RequestBody AuctionBid bid) {
		try {
			return ResponseEntity.ok(auctionService.placeBid(bid));
		} catch (IllegalStateException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@GetMapping("/players/{auctionPlayerId}/bids")
	public ResponseEntity<List<AuctionBid>> getBidsForPlayer(@PathVariable Long auctionPlayerId) {
		return ResponseEntity.ok(auctionService.getBidsForPlayer(auctionPlayerId));
	}

	// ===============================
	// Team Purse
	// ===============================

	@PostMapping("/{auctionId}/purse")
	public ResponseEntity<Long> initializeTeamPurse(@PathVariable Long auctionId, @RequestBody AuctionTeamPurse purse) {
		purse.setAuctionId(auctionId);
		return ResponseEntity.ok(auctionService.initializeTeamPurse(purse));
	}

	@GetMapping("/{auctionId}/purse")
	public ResponseEntity<List<AuctionTeamPurse>> getAuctionTeamPurses(@PathVariable Long auctionId) {
		return ResponseEntity.ok(auctionService.getAuctionTeamPurses(auctionId));
	}

	@GetMapping("/{auctionId}/purse/{teamId}")
	public ResponseEntity<Double> getRemainingPurse(@PathVariable Long auctionId, @PathVariable Long teamId) {
		Optional<Double> remaining = auctionService.getRemainingPurse(auctionId, teamId);
		return remaining.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// ===============================
	// Retention
	// ===============================

	@PostMapping("/{auctionId}/retention/rule")
	public ResponseEntity<Long> defineRetentionRule(@PathVariable Long auctionId,
			@RequestBody AuctionRetentionRule rule) {
		rule.setAuctionId(auctionId);
		return ResponseEntity.ok(auctionService.defineRetentionRule(rule));
	}

	@GetMapping("/{auctionId}/retention/rule")
	public ResponseEntity<AuctionRetentionRule> getRetentionRule(@PathVariable Long auctionId) {
		Optional<AuctionRetentionRule> rule = auctionService.getRetentionRule(auctionId);
		return rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/{auctionId}/retention/players")
	public ResponseEntity<?> retainPlayer(@PathVariable Long auctionId,
			@RequestBody AuctionRetainedPlayer retainedPlayer) {
		try {
			retainedPlayer.setAuctionId(auctionId);
			return ResponseEntity.ok(auctionService.retainPlayer(retainedPlayer));
		} catch (IllegalStateException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@GetMapping("/{auctionId}/retention/players")
	public ResponseEntity<List<AuctionRetainedPlayer>> getRetainedPlayers(@PathVariable Long auctionId) {
		return ResponseEntity.ok(auctionService.getRetainedPlayers(auctionId));
	}

	@GetMapping("/{auctionId}/retention/teams/{teamId}/players")
	public ResponseEntity<List<AuctionRetainedPlayer>> getRetainedPlayersForTeam(@PathVariable Long auctionId,
			@PathVariable Long teamId) {
		return ResponseEntity.ok(auctionService.getRetainedPlayersForTeam(auctionId, teamId));
	}

	// ===============================
	// Bulk Retention (Prototype)
	// ===============================

	@PostMapping("/{auctionId}/retention/players/bulk")
	public ResponseEntity<?> autoRetainPlayers(@PathVariable Long auctionId,
			@RequestBody List<AuctionRetainedPlayer> playersToRetain) {
		try {
			auctionService.autoRetainPlayers(auctionId, playersToRetain);
			return ResponseEntity.ok().build();
		} catch (IllegalStateException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	// ------------------------------
	// Unsold Players
	// ------------------------------
	@GetMapping("/{auctionId}/players/unsold")
	public ResponseEntity<List<AuctionPlayer>> getUnsoldPlayers(@PathVariable Long auctionId) {
		return ResponseEntity.ok(auctionService.getUnsoldPlayers(auctionId));
	}

	// ------------------------------
	// Auction Summary
	// ------------------------------
	@GetMapping("/{auctionId}/summary")
	public ResponseEntity<AuctionSummary> getAuctionSummary(@PathVariable Long auctionId) {
		return ResponseEntity.ok(auctionService.getAuctionSummary(auctionId));
	}

}
