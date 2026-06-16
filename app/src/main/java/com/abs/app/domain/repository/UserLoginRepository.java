package com.abs.app.domain.repository;

import com.abs.app.domain.entity.UserLogin;
import com.abs.app.domain.entity.enums.LoginProvider;

import java.util.Optional;
import java.util.List;

public interface UserLoginRepository {

    Optional<UserLogin> findByProviderAndProviderId(LoginProvider provider, String providerId);

    List<UserLogin> findAllByUserId(String userId);

    UserLogin save(UserLogin userLogin);

    void deleteById(Long id);
}