package com.abs.app.application.admin.category.query;

import com.abs.app.application.admin.category.dto.CategoryResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GetCategoryListQueryHandler {
    private final CategoryRepository categoryRepository;

    public PageResponse<CategoryResponseDto> handle(GetCategoryListQuery query) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> categoryListFilter = categoryList.stream().
                filter(ca -> query.getKeyword() == null || ca.getName().toLowerCase().contains(query.getKeyword().toLowerCase()))
                .toList();

        int total = categoryListFilter.size();
        List<Category> pageFilterList = PaginationUtil.paginate(categoryListFilter, query.getPage(), query.getSize());
        int limit = query.getSize();
        int page = query.getPage();

        List<CategoryResponseDto> items = pageFilterList.stream().map(CategoryMapper::toCategoryResponse).toList();

        return new PageResponse<CategoryResponseDto>(items, total, page, limit);
    }
}
