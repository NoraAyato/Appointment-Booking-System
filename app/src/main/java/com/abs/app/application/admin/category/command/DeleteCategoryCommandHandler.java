package com.abs.app.application.category.command;

import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryCommandHandler {
    private final CategoryRepository categoryRepository;

    public void handler(String id) {
        if(categoryRepository.findById(id).isPresent())
        {
            categoryRepository.deleteById(id);
        } else throw new ResourceNotFoundException(CategoryConstant.NOT_EXIST);

    }

}
