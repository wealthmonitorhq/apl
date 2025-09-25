package com.apl.service;

import org.springframework.stereotype.Service;

import com.apl.repository.TournamentRepository;
import com.apl.repository.model.Auction;
import com.apl.repository.model.Tournament;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

	private final TournamentRepository repository;
	private final AuctionService auctionService;

	public TournamentService(TournamentRepository repository, AuctionService auctionService) {
		this.repository = repository;
		this.auctionService = auctionService;
	}

	public List<Tournament> getAllTournaments() {
		return repository.findAll();
	}

	public Optional<Tournament> getTournamentById(Long id) {
		return repository.findById(id);
	}

	public Tournament createTournament(Tournament tournament) {
		repository.save(tournament);
		return tournament;
	}

	public Tournament updateTournament(Tournament tournament) {
		repository.update(tournament);
		return tournament;
	}

	public boolean deleteTournament(Long id) {
		return repository.deleteById(id) > 0;
	}

	// ----------------- Auction Integration -----------------
	public List<Auction> getAuctionsForTournament(Long tournamentId) {
		return auctionService.getTournamentsAuctions(tournamentId);
	}

	public boolean registerPlayerForTournament(Long tournamentId, Long playerId, String role) {
		return repository.registerPlayer(tournamentId, playerId, role) > 0;
	}

	public List<Long> getTournamentPlayers(Long tournamentId) {
		return repository.getPlayersForTournament(tournamentId);
	}

}
