package com.abs.app.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.app.domain.entity.Reviews;

public interface ReviewsJpaRepository extends JpaRepository<Reviews, String> {
}
