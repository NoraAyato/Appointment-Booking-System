package com.abs.app.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.repository.ReviewRepository;
import com.abs.app.infrastructure.persistence.jpa.ReviewJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public List<Reviews> findAll() {
        return reviewJpaRepository.findAll();
    }

    @Override
    public Optional<Reviews> findById(String id) {
        return reviewJpaRepository.findById(id);
    }

    @Override
    public Reviews save(Reviews review) {
        return reviewJpaRepository.save(review);
    }
}
