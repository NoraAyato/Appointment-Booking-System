package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.application.user.category.dto.CategoryOptionResponse;
import com.abs.app.domain.entity.Category;

public class CategoryMapper {
    public static CategoryResponseDto toCategoryResponse(Category category) {
        CategoryResponseDto Dto = new CategoryResponseDto();
        Dto.setId(category.getId());
        Dto.setName(category.getName());
        Dto.setTagColor(category.getTagColor());
        Dto.setDescription(category.getDescription());
        Dto.setTotalService(category.getServices() != null ? category.getServices().size() : 0);
        return Dto;
    }

    public static CategoryOptionResponse toCategoryOptionResponse(Category category) {
        CategoryOptionResponse Dto = new CategoryOptionResponse();
        Dto.setId(category.getId());
        Dto.setName(category.getName());
        return Dto;
    }
}
