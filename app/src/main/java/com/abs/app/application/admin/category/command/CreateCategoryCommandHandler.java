package com.abs.app.application.admin.category.command;

import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreateCategoryCommandHandler {
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto handle(CreateCategoryCommand command) {
        if(categoryRepository.findByName(command.getName()).isPresent())
        {
            throw new DuplicateResourceException(CategoryConstant.DUPLICATE_RESOURCE);
        }
        Category newCategory = new Category();
        newCategory.setId(GenerateIdUtil.GenerateId(CategoryConstant.SALT_TAG, CategoryConstant.STRING_LIMIT));
        newCategory.setName(command.getName());
        newCategory.setDescription(command.getDescription());
        newCategory.setServices(new ArrayList<>());

        Category saveCategory = categoryRepository.save(newCategory);

        return CategoryMapper.toCategoryResponse(saveCategory);
    }
}
