package com.abs.app.application.admin.category.query;

import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GetCategoryListQueryHandler {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponseDto> handle(GetCategoryListQuery query) {
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getSize(),
                Sort.by("name").ascending());
        Page<com.abs.app.domain.entity.Category> categories = categoryRepository.search(query.getKeyword(), pageable);

        return PaginationUtil.toPageResponse(
                categories,
                CategoryMapper::toCategoryResponse,
                query.getPage(),
                query.getSize());
    }
}
