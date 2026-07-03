package com.abs.app.presentation.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.category.dto.CategoryOptionResponse;
import com.abs.app.application.user.category.query.GetCategoryOptionQueryHandler;
import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/public/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final GetCategoryOptionQueryHandler getCategoryOptionQueryHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryOptionResponse>>> getCategoryOptions() {
        var categoryOptions = getCategoryOptionQueryHandler.handle();
        return ResponseEntity.ok(
                new ApiResponse<>(true, CategoryConstant.GET_SUCCESS, categoryOptions));
    }
}
