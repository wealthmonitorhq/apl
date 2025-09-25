package com.apl.service;

import org.springframework.stereotype.Service;

import com.apl.repository.RoleRepository;
import com.apl.repository.model.Role;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public Optional<Role> getRoleById(Long id) {
		return roleRepository.findById(id);
	}

	public Optional<Role> getRoleByName(String name) {
		return roleRepository.findByName(name);
	}

	public Role createRole(Role role) {
		// you can add validation here (e.g. prevent duplicate role names)
		roleRepository.save(role);
		return role;
	}

	public boolean deleteRole(Long id) {
		return roleRepository.deleteById(id) > 0;
	}
}
