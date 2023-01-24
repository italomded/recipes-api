package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    UserDetails findByUsername(String username);

    boolean existsByUsername(String username);

    Page<ApplicationUser> findByRole_ID(long id, Pageable pageable);
}
