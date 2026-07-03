package com.abs.app.application.admin.category.command;

import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryCommandHandler {
    private final CategoryRepository categoryRepository;

    public void handle(String id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CategoryConstant.NOT_EXIST));

        if (!category.getServices().isEmpty()) {
            throw new ResourceNotFoundException(CategoryConstant.CATEGORY_HAS_SERVICE);
        }
        categoryRepository.deleteById(id);
    }

}
