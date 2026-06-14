package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);
}
