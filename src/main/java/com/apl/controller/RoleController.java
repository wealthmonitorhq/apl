package com.apl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apl.repository.model.Role;
import com.apl.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping
	public ResponseEntity<List<Role>> getAllRoles() {
		return ResponseEntity.ok(roleService.getAllRoles());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
		return roleService.getRoleById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		Role created = roleService.createRole(role);
		return ResponseEntity.ok(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
		if (roleService.deleteRole(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
