package com.abs.app.domain.repository;

import java.util.List;
import java.util.Optional;

import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.enums.RoleEnum;

public interface RoleRepository {
    Optional<Role> findByRoleName(RoleEnum name);

    Optional<Role> findById(Long id);

    List<Role> findAll();

    boolean existsByRoleName(RoleEnum roleName);

    void save(Role role);

    void deleteById(Long id);
}
