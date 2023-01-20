package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    UserDetails findByUsername(String username);
}
