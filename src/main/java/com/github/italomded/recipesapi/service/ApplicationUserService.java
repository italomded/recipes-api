package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;
import com.github.italomded.recipesapi.dto.form.UserChangePasswordForm;
import com.github.italomded.recipesapi.dto.form.UserForm;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.RoleRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class ApplicationUserService {
    private ApplicationUserRepository applicationUserRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, RoleRepository roleRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.roleRepository = roleRepository;
    }

    public Page<ApplicationUser> getAllUsers(Pageable pageable) {
        return applicationUserRepository.findAll(pageable);
    }

    public ApplicationUser getUserById(Long id) {
        return applicationUserRepository.getReferenceById(id);
    }

    @Transactional
    public ApplicationUser createUser(UserForm form) {
        verifyIfUserNameAlreadyExists(form);

        String passwordEncoded = new BCryptPasswordEncoder().encode(form.password());
        Role usrRole = roleRepository.getReferenceByRepresentation("ROLE_USR");
        ApplicationUser userCreated = new ApplicationUser(form.username(), passwordEncoded, usrRole);
        usrRole.addUser(userCreated);

        userCreated = applicationUserRepository.save(userCreated);
        return userCreated;
    }

    @Transactional
    public ApplicationUser changeUserPassword(UserChangePasswordForm form, ApplicationUser user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(form.oldPassword(), user.getPassword())) {
            throw new BusinessRuleException(ApplicationUser.class, "incorrect password");
        }

        user.setPassword(encoder.encode(form.newPassword()));
        user = applicationUserRepository.save(user);
        return user;
    }

    @Transactional
    public void deleteUser(long id) {
        applicationUserRepository.deleteById(id);
    }

    private void verifyIfUserNameAlreadyExists(UserForm form) {
        boolean exists = applicationUserRepository.existsByUsername(form.username());
        if (exists) {
            try {
                Field username = form.getClass().getDeclaredField("username");
                throw new DataValidationException("username already exists", username);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
