package com.abs.app.domain.repository;

import java.util.List;
import java.util.Optional;

import com.abs.app.domain.entity.Reviews;

public interface ReviewRepository {
    List<Reviews> findAll();
    
    Optional<Reviews> findById(String id);
    
    void save(Reviews review);
}
