package com.apl.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apl.repository.*;
import com.apl.repository.model.*;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final AuctionPlayerRepository auctionPlayerRepository;
	private final AuctionBidRepository auctionBidRepository;
	private final AuctionTeamPurseRepository auctionTeamPurseRepository;
	private final AuctionRetentionRuleRepository auctionRetentionRuleRepository;
	private final AuctionRetainedPlayerRepository auctionRetainedPlayerRepository;

	public AuctionService(AuctionRepository auctionRepository, AuctionPlayerRepository auctionPlayerRepository,
			AuctionBidRepository auctionBidRepository, AuctionTeamPurseRepository auctionTeamPurseRepository,
			AuctionRetentionRuleRepository auctionRetentionRuleRepository,
			AuctionRetainedPlayerRepository auctionRetainedPlayerRepository) {
		this.auctionRepository = auctionRepository;
		this.auctionPlayerRepository = auctionPlayerRepository;
		this.auctionBidRepository = auctionBidRepository;
		this.auctionTeamPurseRepository = auctionTeamPurseRepository;
		this.auctionRetentionRuleRepository = auctionRetentionRuleRepository;
		this.auctionRetainedPlayerRepository = auctionRetainedPlayerRepository;
	}

	// ===============================
	// Auction Lifecycle
	// ===============================

	public Long createAuction(Auction auction) {
		auction.setStatus("scheduled"); // default status
		return auctionRepository.save(auction);
	}

	public Optional<Auction> getAuction(Long auctionId) {
		return auctionRepository.findById(auctionId);
	}

	public List<Auction> getTournamentsAuctions(Long tournamentId) {
		return auctionRepository.findByTournamentId(tournamentId);
	}

	public int updateAuctionStatus(Long auctionId, String status) {
		return auctionRepository.updateStatus(auctionId, status);
	}

	// ===============================
	// Auction Players
	// ===============================

	public Long registerPlayerForAuction(AuctionPlayer player) {
		// fetch auction to get tournamentId
		Auction auction = auctionRepository.findById(player.getAuctionId())
				.orElseThrow(() -> new IllegalStateException("Auction not found"));

		player.setTournamentId(auction.getTournamentId()); // link auction → tournament
		player.setStatus("unsold"); // default status

		auctionPlayerRepository.save(player);
		return player.getId();
	}

	public List<AuctionPlayer> getAuctionPlayers(Long auctionId) {
		return auctionPlayerRepository.findByAuctionId(auctionId);
	}

	@Transactional
	public boolean markPlayerSold(Long auctionPlayerId, Long teamId, double finalPrice) {
		Optional<AuctionPlayer> playerOpt = auctionPlayerRepository.findByAuctionPlayer(auctionPlayerId);
		if (playerOpt.isEmpty())
			throw new IllegalStateException("Auction player not found!");

		AuctionPlayer player = playerOpt.get();
		Long auctionId = player.getAuctionId();

		// check auction status
		Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);
		if (auctionOpt.isEmpty() || !"live".equalsIgnoreCase(auctionOpt.get().getStatus())) {
			throw new IllegalStateException("Cannot sell player: Auction is not live!");
		}

		// deduct purse
		int deducted = auctionTeamPurseRepository.deductPurse(auctionId, teamId, finalPrice);
		if (deducted == 0)
			throw new IllegalStateException("Insufficient purse!");

		// mark player sold
		int updated = auctionPlayerRepository.markPlayerSold(auctionPlayerId, teamId, finalPrice);
		return updated > 0;
	}

	// ===============================
	// Bidding
	// ===============================

	@Transactional
	public Long placeBid(AuctionBid bid) {
		Optional<AuctionPlayer> playerOpt = auctionPlayerRepository.findByAuctionPlayer(bid.getAuctionPlayerId());
		if (playerOpt.isEmpty())
			throw new IllegalStateException("Auction player not found!");

		AuctionPlayer player = playerOpt.get();
		Long auctionId = player.getAuctionId();

		// ensure auction is live
		Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);
		if (auctionOpt.isEmpty() || !"live".equalsIgnoreCase(auctionOpt.get().getStatus())) {
			throw new IllegalStateException("Auction is not live!");
		}

		// ensure player is unsold
		if (!"unsold".equalsIgnoreCase(player.getStatus())) {
			throw new IllegalStateException("Cannot bid: Player already sold or withdrawn!");
		}

		// validate team purse
		double remainingPurse = auctionTeamPurseRepository.getRemainingPurse(auctionId, bid.getTeamId()).orElse(0.0);
		if (remainingPurse < bid.getBidAmount()) {
			throw new IllegalStateException("Insufficient purse to place bid!");
		}

		return auctionBidRepository.save(bid);
	}

	public List<AuctionBid> getBidsForPlayer(Long auctionPlayerId) {
		return auctionBidRepository.findByAuctionPlayerId(auctionPlayerId);
	}

	// ===============================
	// Team Purse
	// ===============================

	public Long initializeTeamPurse(AuctionTeamPurse purse) {
		return auctionTeamPurseRepository.save(purse);
	}

	public List<AuctionTeamPurse> getAuctionTeamPurses(Long auctionId) {
		return auctionTeamPurseRepository.findByAuction(auctionId);
	}

	public Optional<Double> getRemainingPurse(Long auctionId, Long teamId) {
		return auctionTeamPurseRepository.getRemainingPurse(auctionId, teamId);
	}

	@Transactional
	public void initializeTeamPursesForAuction(Long auctionId, List<Long> teamIds, double initialPurse) {
		for (Long teamId : teamIds) {
			AuctionTeamPurse purse = new AuctionTeamPurse();
			purse.setAuctionId(auctionId);
			purse.setTeamId(teamId);
			purse.setInitialPurse(initialPurse);
			purse.setRemainingPurse(initialPurse);
			auctionTeamPurseRepository.save(purse);
		}
	}

	// ===============================
	// Retention
	// ===============================

	public Long defineRetentionRule(AuctionRetentionRule rule) {
		return auctionRetentionRuleRepository.save(rule);
	}

	public Optional<AuctionRetentionRule> getRetentionRule(Long auctionId) {
		return auctionRetentionRuleRepository.findByAuction(auctionId);
	}

	@Transactional
	public Long retainPlayer(AuctionRetainedPlayer retainedPlayer) {
		Long auctionId = retainedPlayer.getAuctionId();
		Long teamId = retainedPlayer.getTeamId();

		// Fetch the retention rule for this auction
		AuctionRetentionRule rule = auctionRetentionRuleRepository.findByAuction(auctionId)
				.orElseThrow(() -> new IllegalStateException("Retention rule not defined"));

		// Check how many players the team has already retained
		List<AuctionRetainedPlayer> existing = auctionRetainedPlayerRepository.findByAuctionAndTeam(auctionId, teamId);
		if (existing.size() >= rule.getMaxRetained()) { // use maxRetained from schema
			throw new IllegalStateException("Team has already retained maximum allowed players");
		}

		// Check remaining purse
		double remainingPurse = auctionTeamPurseRepository.getRemainingPurse(auctionId, teamId).orElse(0.0);
		if (remainingPurse < retainedPlayer.getRetentionPrice()) {
			throw new IllegalStateException("Insufficient purse for retention!");
		}

		// Deduct the purse
		int deducted = auctionTeamPurseRepository.deductPurse(auctionId, teamId, retainedPlayer.getRetentionPrice());
		if (deducted == 0) {
			throw new IllegalStateException("Failed to deduct purse!");
		}

		// Save the retained player
		return auctionRetainedPlayerRepository.save(retainedPlayer);
	}

	public List<AuctionRetainedPlayer> getRetainedPlayers(Long auctionId) {
		return auctionRetainedPlayerRepository.findByAuction(auctionId);
	}

	public List<AuctionRetainedPlayer> getRetainedPlayersForTeam(Long auctionId, Long teamId) {
		return auctionRetainedPlayerRepository.findByAuctionAndTeam(auctionId, teamId);
	}

	// ===============================
	// Bulk Auction Operations
	// ===============================

	/**
	 * Automatically retain all eligible players for teams based on retention rules.
	 */
	@Transactional
	public void autoRetainPlayers(Long auctionId, List<AuctionRetainedPlayer> playersToRetain) {
		AuctionRetentionRule rule = auctionRetentionRuleRepository.findByAuction(auctionId)
				.orElseThrow(() -> new IllegalStateException("Retention rule not defined"));

		for (AuctionRetainedPlayer player : playersToRetain) {
			List<AuctionRetainedPlayer> teamExisting = auctionRetainedPlayerRepository.findByAuctionAndTeam(auctionId,
					player.getTeamId());

			if (teamExisting.size() < rule.getMaxRetained()) { // again, use maxRetained
				retainPlayer(player);
			}
		}
	}
}
