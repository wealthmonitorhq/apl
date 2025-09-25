package com.apl.service;

import org.springframework.stereotype.Service;

import com.apl.repository.TeamRepository;
import com.apl.repository.model.Team;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

	private final TeamRepository teamRepository;

	public TeamService(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	public List<Team> getAllTeams() {
		return teamRepository.findAll();
	}

	public Optional<Team> getTeamById(Long id) {
		return teamRepository.findById(id);
	}

	public Team createTeam(Team team) {
		teamRepository.save(team);
		return team;
	}

	public Team updateTeam(Team team) {
		teamRepository.update(team);
		return team;
	}

	public boolean deleteTeam(Long id) {
		return teamRepository.deleteById(id) > 0;
	}
}
