package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @InjectMocks
    private RoleService roleService;

    private AutoCloseable closeable;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowAExceptionIfRoleDoesntExistsOnChangeRole() {
        Mockito.when(applicationUserRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.changeUserRole(1L, 2L));
    }

    @Test
    void shouldThrowAExceptionIfUserDoesntExistsOnChangeRole() {
        Mockito.when(applicationUserRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new ApplicationUser());
        Mockito.when(roleRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.changeUserRole(1L, 2L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }
}
