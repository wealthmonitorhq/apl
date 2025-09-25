package com.apl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.repository.model.Team;
import com.apl.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@GetMapping
	public ResponseEntity<List<Team>> getAllTeams() {
		return ResponseEntity.ok(teamService.getAllTeams());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
		return teamService.getTeamById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Team> createTeam(@RequestBody Team team) {
		return ResponseEntity.ok(teamService.createTeam(team));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
		team.setId(id);
		return ResponseEntity.ok(teamService.updateTeam(team));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
		if (teamService.deleteTeam(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
