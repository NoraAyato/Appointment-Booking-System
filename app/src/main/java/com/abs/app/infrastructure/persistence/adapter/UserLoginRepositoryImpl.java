package com.abs.app.infrastructure.persistence.adapter;

import com.abs.app.domain.entity.UserLogin;
import com.abs.app.domain.entity.enums.LoginProvider;
import com.abs.app.domain.repository.UserLoginRepository;
import com.abs.app.infrastructure.persistence.jpa.JpaUserLoginRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserLoginRepositoryImpl implements UserLoginRepository {

    private final JpaUserLoginRepository userLoginJpaRepository;

    @Override
    public Optional<UserLogin> findByProviderAndProviderId(LoginProvider provider, String providerId) {
        return userLoginJpaRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public List<UserLogin> findAllByUserId(String userId) {
        return userLoginJpaRepository.findAllByUser_UserId(userId);
    }

    @Override
    public UserLogin save(UserLogin userLogin) {
        return userLoginJpaRepository.save(userLogin);
    }

    @Override
    public void deleteById(Long id) {
        userLoginJpaRepository.deleteById(id);
    }
}