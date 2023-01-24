package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
