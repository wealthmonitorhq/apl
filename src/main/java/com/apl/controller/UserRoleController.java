package com.apl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.repository.model.UserRole;
import com.apl.service.UserRoleService;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

	private final UserRoleService userRoleService;

	public UserRoleController(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<UserRole>> getRolesForUser(@PathVariable Long userId) {
		return ResponseEntity.ok(userRoleService.getRolesForUser(userId));
	}

	@PostMapping("/assign")
	public ResponseEntity<String> assignRole(@RequestParam Long userId, @RequestParam Long roleId) {
		if (userRoleService.assignRole(userId, roleId)) {
			return ResponseEntity.ok("Role assigned successfully");
		}
		return ResponseEntity.badRequest().body("Failed to assign role");
	}

	@DeleteMapping("/remove")
	public ResponseEntity<String> removeRole(@RequestParam Long userId, @RequestParam Long roleId) {
		if (userRoleService.removeRole(userId, roleId)) {
			return ResponseEntity.ok("Role removed successfully");
		}
		return ResponseEntity.badRequest().body("Failed to remove role");
	}
}
