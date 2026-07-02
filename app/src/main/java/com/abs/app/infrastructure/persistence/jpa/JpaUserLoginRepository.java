package com.abs.app.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abs.app.domain.entity.UserLogin;
import com.abs.app.domain.entity.enums.LoginProvider;

import java.util.Optional;
import java.util.List;

@Repository
public interface JpaUserLoginRepository extends JpaRepository<UserLogin, Long> {
    Optional<UserLogin> findByProviderAndProviderId(LoginProvider provider, String providerId);

    List<UserLogin> findAllByUser_UserId(String userId);
}