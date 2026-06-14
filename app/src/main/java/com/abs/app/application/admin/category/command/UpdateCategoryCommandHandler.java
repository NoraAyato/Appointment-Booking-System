package com.abs.app.application.category.command;

import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCategoryCommandHandler {
    private final CategoryRepository categoryRepository;

    public Category handle(UpdateCategoryCommand command) {
        if(categoryRepository.findByName(command.getName()).isPresent())
        {
            throw new DuplicateResourceException(CategoryConstant.DUPLICATE_RESOURCE);
        }

        Category categoryEdit = categoryRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(CategoryConstant.NOT_EXIST));

        categoryEdit.setName(command.getName());
        categoryEdit.setDescription(command.getDescription());

        return categoryRepository.save(categoryEdit);
    }
}
