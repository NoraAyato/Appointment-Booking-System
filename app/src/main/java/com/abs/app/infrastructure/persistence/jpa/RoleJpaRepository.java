package com.abs.app.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.enums.RoleEnum;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleEnum name);

    boolean existsByRoleName(RoleEnum roleName);
}
