package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.ApplicationUserDTO;
import com.github.italomded.recipesapi.dto.ApplicationUserDetailedDTO;
import com.github.italomded.recipesapi.dto.form.UserChangePasswordForm;
import com.github.italomded.recipesapi.dto.form.UserForm;
import com.github.italomded.recipesapi.dto.authentication.TokenDTO;
import com.github.italomded.recipesapi.security.TokenService;
import com.github.italomded.recipesapi.service.ApplicationUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/user")
public class ApplicationUserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApplicationUserService applicationUserService;

    @GetMapping
    public ResponseEntity<Page<ApplicationUserDTO>> getAllUsers(Pageable pageable) {
        Page<ApplicationUser> allUsers = applicationUserService.getAllUsers(pageable);
        Page<ApplicationUserDTO> allUsersDTO = allUsers.map(ApplicationUserDTO::new);
        return ResponseEntity.ok(allUsersDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApplicationUserDetailedDTO> getUser(@PathVariable long id, UriComponentsBuilder uriComponentsBuilder) {
        ApplicationUser user = applicationUserService.getUserById(id);
        URI userRecipesURI = uriComponentsBuilder.path("/api/recipe/user/{id}").buildAndExpand(user.getID()).toUri();
        return ResponseEntity.ok(new ApplicationUserDetailedDTO(user, userRecipesURI));
    }

    @PostMapping("login")
    public ResponseEntity<TokenDTO> authenticate(@RequestBody @Valid UserForm form) {
        UsernamePasswordAuthenticationToken authenticationData = new UsernamePasswordAuthenticationToken(form.username(), form.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationData);

        ApplicationUser user = (ApplicationUser) authenticate.getPrincipal();
        String tokenJWT = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenDTO(tokenJWT));
    }

    @PostMapping("register")
    public ResponseEntity<ApplicationUserDTO> createUser(@RequestBody @Valid UserForm form, UriComponentsBuilder uriComponentsBuilder) {
        ApplicationUser applicationUser = applicationUserService.createUser(form);

        URI location = uriComponentsBuilder.path("/api/user/{id}").buildAndExpand(applicationUser.getID()).toUri();
        return ResponseEntity.created(location).body(new ApplicationUserDTO(applicationUser));
    }

    @PutMapping
    public ResponseEntity<ApplicationUserDTO> updateUserPassword(@RequestBody @Valid UserChangePasswordForm form) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = applicationUserService.changeUserPassword(form, user);

        return ResponseEntity.ok(new ApplicationUserDTO(user));
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_ADM")
    public ResponseEntity deleteUser(@PathVariable long id) {
        applicationUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
