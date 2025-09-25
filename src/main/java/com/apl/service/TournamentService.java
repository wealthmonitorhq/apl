package com.apl.service;

import org.springframework.stereotype.Service;

import com.apl.repository.TournamentRepository;
import com.apl.repository.model.Tournament;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

	private final TournamentRepository repository;

	public TournamentService(TournamentRepository repository) {
		this.repository = repository;
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
}
