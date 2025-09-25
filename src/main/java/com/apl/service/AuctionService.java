package com.apl.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apl.repository.AuctionBidRepository;
import com.apl.repository.AuctionPlayerRepository;
import com.apl.repository.AuctionRepository;
import com.apl.repository.AuctionRetainedPlayerRepository;
import com.apl.repository.AuctionRetentionRuleRepository;
import com.apl.repository.AuctionTeamPurseRepository;
import com.apl.repository.model.Auction;
import com.apl.repository.model.AuctionBid;
import com.apl.repository.model.AuctionPlayer;
import com.apl.repository.model.AuctionRetainedPlayer;
import com.apl.repository.model.AuctionRetentionRule;
import com.apl.repository.model.AuctionTeamPurse;

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
	// Auction lifecycle
	// ===============================

	public Long createAuction(Auction auction) {
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
		return auctionPlayerRepository.save(player);
	}

	public List<AuctionPlayer> getAuctionPlayers(Long auctionId) {
		return auctionPlayerRepository.findByAuctionId(auctionId);
	}

	@Transactional
	public boolean markPlayerSold(Long auctionPlayerId, Long teamId, double finalPrice) {
		// update player status and sold price
		int updated = auctionPlayerRepository.markPlayerSold(auctionPlayerId, teamId, finalPrice);
		if (updated > 0) {
			// get auctionId from player
			Optional<AuctionPlayer> playerOpt = auctionPlayerRepository.findByAuctionPlayer(auctionPlayerId);
			if (playerOpt.isEmpty()) {
				throw new IllegalStateException("Auction player not found!");
			}
			Long auctionId = playerOpt.get().getAuctionId();

			// deduct purse from team for that auction
			auctionTeamPurseRepository.deductPurse(auctionId, teamId, finalPrice);
			return true;
		}
		return false;
	}

	// ===============================
	// Bidding
	// ===============================

	@Transactional
	public Long placeBid(AuctionBid bid) {
		// fetch auctionId from auctionPlayerId
		Optional<AuctionPlayer> playerOpt = auctionPlayerRepository.findByAuctionPlayer(bid.getAuctionPlayerId());
		if (playerOpt.isEmpty()) {
			throw new IllegalStateException("Auction player not found!");
		}
		Long auctionId = playerOpt.get().getAuctionId();

		// validate team has enough purse
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

	// ===============================
	// Retention
	// ===============================

	public Long defineRetentionRule(AuctionRetentionRule rule) {
		return auctionRetentionRuleRepository.save(rule);
	}

	public Optional<AuctionRetentionRule> getRetentionRule(Long auctionId) {
		return auctionRetentionRuleRepository.findByAuction(auctionId);
	}

	public Long retainPlayer(AuctionRetainedPlayer retainedPlayer) {
		return auctionRetainedPlayerRepository.save(retainedPlayer);
	}

	public List<AuctionRetainedPlayer> getRetainedPlayers(Long auctionId) {
		return auctionRetainedPlayerRepository.findByAuction(auctionId);
	}

	public List<AuctionRetainedPlayer> getRetainedPlayersForTeam(Long auctionId, Long teamId) {
		return auctionRetainedPlayerRepository.findByAuctionAndTeam(auctionId, teamId);
	}
}
