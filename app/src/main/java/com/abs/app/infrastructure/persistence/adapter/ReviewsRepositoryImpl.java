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
    private final ReviewJpaRepository reviewsJpaRepository;
    public List<Reviews> findAll(){
        return reviewsJpaRepository.findAll();
    }
    public Optional<Reviews> findById(String id){
        return reviewsJpaRepository.findById(id);
    }
    public void save(Reviews reviews){
        reviewsJpaRepository.save(reviews);
    }
}
