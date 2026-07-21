package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.infrastructure.persistence.jpa.JpaUserLoginRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.persistence.jpa.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(String id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userJpaRepository.findByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<User> findByIdWithRole(String userId) {
      return userJpaRepository.findById(userId);
    }

    @Override
    public Page<User> findBySearchAndRole(String search, RoleEnum role, UserStatus status, Pageable pageable) {
        return userJpaRepository.findBySearchAndRole(search, role, status, pageable);
    }

    @Override
    public List<User> findAllByRoleRoleNameAndStatus(RoleEnum role, UserStatus status) {
        return userJpaRepository.findAllByRoleRoleNameAndStatus(role, status);
    }

    @Override
    public long countByRoleAndStatus(RoleEnum role, UserStatus status) {
        return userJpaRepository.countByRoleAndStatus(role, status);
    }

    @Override
    public long countByRoleAndStatusAndCreatedAtBetween(
            RoleEnum role,
            UserStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt) {
        return userJpaRepository.countByRoleAndStatusAndCreatedAtBetween(role, status, startAt, endAt);
    }

    @Override
    public long countActiveStaffWithoutApprovedShiftOnDate(LocalDate date) {
        return userJpaRepository.countActiveStaffWithoutApprovedShiftOnDate(date);
    }
}
