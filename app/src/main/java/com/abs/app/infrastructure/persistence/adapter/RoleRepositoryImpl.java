package com.abs.app.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.repository.RoleRepository;
import com.abs.app.infrastructure.persistence.jpa.RoleJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<Role> findByRoleName(RoleEnum name) {
        return roleJpaRepository.findByRoleName(name);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll();

    }

    @Override
    public void save(Role role) {
        roleJpaRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        roleJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByRoleName(RoleEnum roleName) {
        return roleJpaRepository.existsByRoleName(roleName);
    }

}
