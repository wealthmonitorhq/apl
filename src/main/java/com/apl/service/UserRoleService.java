package com.apl.service;

import org.springframework.stereotype.Service;

import com.apl.repository.UserRoleRepository;
import com.apl.repository.model.UserRole;

import java.util.List;

@Service
public class UserRoleService {

	private final UserRoleRepository userRoleRepository;

	public UserRoleService(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}

	public List<UserRole> getRolesForUser(Long userId) {
		return userRoleRepository.findByUserId(userId);
	}

	public boolean assignRole(Long userId, Long roleId) {
		return userRoleRepository.assignRoleToUser(userId, roleId) > 0;
	}

	public boolean removeRole(Long userId, Long roleId) {
		return userRoleRepository.removeRoleFromUser(userId, roleId) > 0;
	}
}
