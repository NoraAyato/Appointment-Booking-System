package com.abs.app.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;

public interface UserRepository {
    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    void deleteById(String id);

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<User> findByIdWithRole(String userId);

    Page<User> findBySearchAndRole(String search, RoleEnum role, UserStatus status, Pageable pageable);
}
