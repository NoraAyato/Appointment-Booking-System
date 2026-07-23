package com.abs.app.application.user.category.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abs.app.application.user.category.dto.CategoryOptionResponse;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.infrastructure.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCategoryOptionQueryHandler {
    private final CategoryRepository categoryRepository;

    public List<CategoryOptionResponse> handle() {
        var categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::toCategoryOptionResponse)
                .collect(Collectors.toList());
    }
}
