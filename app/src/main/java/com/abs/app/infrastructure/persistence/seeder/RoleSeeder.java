package com.abs.app.infrastructure.persistence.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        seedRole(RoleEnum.ADMIN);
        seedRole(RoleEnum.STAFF);
        seedRole(RoleEnum.CUSTOMER);
    }

    private void seedRole(RoleEnum roleEnum) {
        if (!roleRepository.existsByRoleName(roleEnum)) {
            Role role = new Role();
            role.setRoleName(roleEnum);
            roleRepository.save(role);
        }
    }
}
