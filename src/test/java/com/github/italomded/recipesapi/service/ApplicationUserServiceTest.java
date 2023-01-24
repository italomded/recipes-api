package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;
import com.github.italomded.recipesapi.dto.form.UserChangePasswordForm;
import com.github.italomded.recipesapi.dto.form.UserForm;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.RoleRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ApplicationUserServiceTest {
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ApplicationUserService applicationUserService;
    @Captor
    private ArgumentCaptor<ApplicationUser> userCaptor;

    private AutoCloseable closeable;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldEncodeUserPassword() {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.any()))
                .thenReturn(false);
        Mockito.when(roleRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Role("ROLE_USR"));

        UserForm form = new UserForm("username", "password");
        applicationUserService.createUser(form);

        Mockito.verify(applicationUserRepository).save(userCaptor.capture());
        ApplicationUser user = userCaptor.getValue();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Assertions.assertTrue(bCryptPasswordEncoder.matches(form.password(), user.getPassword()));
    }

    @Test
    void mustNotAllowTheCreationOfAUserWithRepeatedUsername() {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.any()))
                .thenReturn(true);

        UserForm form = new UserForm("username", "password");
        Assertions.assertThrows(DataValidationException.class, () -> applicationUserService.createUser(form));
    }

    @Test
    void shouldChangePasswordIfTheyMatches() {
        ApplicationUser user = createApplicationUser();

        UserChangePasswordForm form = new UserChangePasswordForm("123456", "1234567");
        applicationUserService.changeUserPassword(form, user);

        Mockito.verify(applicationUserRepository).save(userCaptor.capture());
        user = userCaptor.getValue();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Assertions.assertTrue(bCryptPasswordEncoder.matches(form.newPassword(), user.getPassword()));
    }

    @Test
    void shouldThrowAExceptionWhenTryToChangePasswordAndTheyDontMatches() {
        ApplicationUser user = createApplicationUser();
        UserChangePasswordForm form = new UserChangePasswordForm("1234567", "123456");
        Assertions.assertThrows(BusinessRuleException.class, () -> applicationUserService.changeUserPassword(form, user));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private ApplicationUser createApplicationUser() {
        return new ApplicationUser("bob",
                "$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.",
                new Role("ROLE_USR"));
    }
}
