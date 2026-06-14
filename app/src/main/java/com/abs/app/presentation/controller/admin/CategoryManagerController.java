package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.category.command.*;
import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.application.admin.category.dto.CreateCategoryRequestDto;
import com.abs.app.application.admin.category.dto.UpdateCategoryRequestDto;
import com.abs.app.application.admin.category.query.GetCategoryListQuery;
import com.abs.app.application.admin.category.query.GetCategoryListQueryHandler;
import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryManagerController {
    private final GetCategoryListQueryHandler getCategoryListQueryHandler;
    private final CreateCategoryCommandHandler createCategoryCommandHandler;
    private final UpdateCategoryCommandHandler updateCategoryCommandHandler;
    private final DeleteCategoryCommandHandler deleteCategoryCommandHandler;


    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponseDto>>> getCategories(@RequestParam(required = false) String keyword,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "5") int size) {
        PageResponse<CategoryResponseDto> pageResponse = getCategoryListQueryHandler.handle(new GetCategoryListQuery(keyword, page, size));
        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstant.GET_SUCCESS, pageResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(@Valid @RequestBody CreateCategoryRequestDto request) {
        Category category = createCategoryCommandHandler.handle(new CreateCategoryCommand(request.getName(), request.getDescription()));
        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstant.CREATE_SUCCESS, category));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<Category>> update(@PathVariable String id, @Valid @RequestBody UpdateCategoryRequestDto request) {
        Category category = updateCategoryCommandHandler.handle(new UpdateCategoryCommand(id, request.getName(), request.getDescription()));
        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstant.UPDATE_SUCCESS, category));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable String id) {
        deleteCategoryCommandHandler.handle(id);
        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstant.DELETE_SUCCESS, null));
    }
}
