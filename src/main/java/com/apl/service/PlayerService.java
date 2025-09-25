package com.apl.service;

import org.springframework.stereotype.Service;

import com.apl.repository.PlayerRepository;
import com.apl.repository.model.Player;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

	private final PlayerRepository repository;

	public PlayerService(PlayerRepository repository) {
		this.repository = repository;
	}

	public List<Player> getAllPlayers() {
		return repository.findAll();
	}

	public Optional<Player> getPlayerById(Long id) {
		return repository.findById(id);
	}

	public Player createPlayer(Player player) {
		repository.save(player);
		return player;
	}

	public Player updatePlayer(Player player) {
		repository.update(player);
		return player;
	}

	public boolean deletePlayer(Long id) {
		return repository.deleteById(id) > 0;
	}

	public List<Player> getPlayersByTournament(Long tournamentId) {
		return repository.findByTournamentId(tournamentId);
	}

	public List<Player> getPlayersByTeam(Long teamId) {
		return repository.findByTeamId(teamId);
	}

}
