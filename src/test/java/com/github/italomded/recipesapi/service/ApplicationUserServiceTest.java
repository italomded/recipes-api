package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.builder.ApplicationUserBuilder;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;
import com.github.italomded.recipesapi.dto.form.UserChangePasswordForm;
import com.github.italomded.recipesapi.dto.form.UserForm;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.RoleRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import org.apache.catalina.User;
import org.junit.jupiter.api.*;
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
    @DisplayName("Must encode user password on creation")
    void scenario1() {
        //given
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserForm form = createUserForm();

        Mockito.when(applicationUserRepository.existsByUsername(form.username()))
                .thenReturn(false);
        Mockito.when(roleRepository.getReferenceByRepresentation(Mockito.any()))
                .thenReturn(new Role("ROLE_USR"));

        //when
        applicationUserService.createUser(form);

        //then
        Mockito.verify(applicationUserRepository).save(userCaptor.capture());
        ApplicationUser user = userCaptor.getValue();
        Assertions.assertTrue(bCryptPasswordEncoder.matches(form.password(), user.getPassword()));
    }

    @Test
    @DisplayName("Should throw exception when trying to create a user with repeated username")
    void scenario2() {
        //given
        UserForm form = createUserForm();
        Mockito.when(applicationUserRepository.existsByUsername(form.username()))
                .thenReturn(true);

        //when,then
        Assertions.assertThrows(DataValidationException.class, () -> applicationUserService.createUser(form));
    }

    @Test
    @DisplayName("Must change user password if passwords match")
    void scenario3() {
        //given
        ApplicationUser user = ApplicationUserBuilder.builder().withId(1L)
                .withPassword("$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.").build();
        UserChangePasswordForm form = new UserChangePasswordForm("123456", "1234567");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //when
        applicationUserService.changeUserPassword(form, user);

        //then
        Mockito.verify(applicationUserRepository).save(userCaptor.capture());
        user = userCaptor.getValue();
        Assertions.assertTrue(bCryptPasswordEncoder.matches(form.newPassword(), user.getPassword()));
    }

    @Test
    @DisplayName("It should throw an exception if the user tries to change the password and they don't match")
    void scenario4() {
        //given
        ApplicationUser user = ApplicationUserBuilder.builder().withId(1L)
                .withPassword("$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.").build();
        UserChangePasswordForm form = new UserChangePasswordForm("1234567", "123456");

        //when,then
        Assertions.assertThrows(BusinessRuleException.class, () -> applicationUserService.changeUserPassword(form, user));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private UserForm createUserForm() {
        return new UserForm("username", "password");
    }
}
