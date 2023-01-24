package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;
import com.github.italomded.recipesapi.dto.ApplicationUserDTO;
import com.github.italomded.recipesapi.dto.RoleDTO;
import com.github.italomded.recipesapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleDTO>> getAllRoles(Pageable pageable) {
        Page<Role> allRoles = roleService.getAllRoles(pageable);
        Page<RoleDTO> allRolesDTO = allRoles.map(RoleDTO::new);
        return ResponseEntity.ok(allRolesDTO);
    }

    @GetMapping("api/role/{id}")
    @Secured("ROLE_ADM")
    public ResponseEntity<Page<ApplicationUserDTO>> getUsersInRole(@PathVariable long id, Pageable pageable) {
        Page<ApplicationUser> allUsers = roleService.getUsersInRole(id, pageable);
        Page<ApplicationUserDTO> allUsersDTO = allUsers.map(ApplicationUserDTO::new);
        return ResponseEntity.ok(allUsersDTO);
    }

    @PutMapping("{roleId}/{userId}")
    @Secured("ROLE_ADM")
    public ResponseEntity<ApplicationUserDTO> putRole(@PathVariable long roleId, @PathVariable long userId) {
        ApplicationUser user = roleService.changeUserRole(roleId, userId);
        return ResponseEntity.ok(new ApplicationUserDTO(user));
    }
}
