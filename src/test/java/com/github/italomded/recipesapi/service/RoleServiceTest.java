package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
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
    @DisplayName("Should throw an exception when changing roles if the user does not exist")
    void scenario1() {
        //given
        Mockito.when(applicationUserRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.changeUserRole(1L, 2L));
    }

    @Test
    @DisplayName("Should throw an exception when switching roles if it doesn't exist")
    void scenario2() {
        //given
        Mockito.when(applicationUserRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new ApplicationUser());
        Mockito.when(roleRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.changeUserRole(1L, 2L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }
}
