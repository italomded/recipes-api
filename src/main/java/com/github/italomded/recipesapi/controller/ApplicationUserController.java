package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.LoginForm;
import com.github.italomded.recipesapi.dto.authentication.TokenDTO;
import com.github.italomded.recipesapi.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class ApplicationUserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public ResponseEntity authenticate(@RequestBody @Valid LoginForm form) {
        UsernamePasswordAuthenticationToken authenticationData = new UsernamePasswordAuthenticationToken(form.username(), form.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationData);
        ApplicationUser user = (ApplicationUser) authenticate.getPrincipal();
        String tokenJWT = tokenService.generateToken(user);
        return ResponseEntity.ok(new TokenDTO(tokenJWT));
    }

    // TODO: update, delete
}
