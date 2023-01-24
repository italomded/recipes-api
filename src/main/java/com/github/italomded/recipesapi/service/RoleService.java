package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, ApplicationUserRepository applicationUserRepository) {
        this.roleRepository = roleRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public Page<ApplicationUser> getUsersInRole(long id, Pageable pageable) {
        return applicationUserRepository.findByRole_ID(id, pageable);
    }

    @Transactional
    public ApplicationUser changeUserRole(long roleId, long userId) {
        ApplicationUser user = applicationUserRepository.getReferenceById(userId);
        Role role = roleRepository.getReferenceById(roleId);

        user.getRole().removeUser(user);
        role.addUser(user);
        user.setRole(role);

        user = applicationUserRepository.save(user);
        return user;
    }
}
