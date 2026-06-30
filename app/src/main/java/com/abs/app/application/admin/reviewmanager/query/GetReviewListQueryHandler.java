package com.abs.app.application.admin.reviewmanager.query;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.reviewmanager.dto.ReviewResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.ReviewRepository;
import com.abs.app.infrastructure.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetReviewListQueryHandler {

    private final ReviewRepository reviewRepository;

    public PageResponse<ReviewResponseDto> handle(GetReviewListQuery query) {
        List<Reviews> reviewList = reviewRepository.findAll();

        List<Reviews> reviewListFilter = reviewList.stream()
            .filter(review -> {
                boolean matchKeyword = true;
                if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
                    String kw = query.getKeyword().toLowerCase();
                    boolean matchDesc = review.getDescription() != null && review.getDescription().toLowerCase().contains(kw);
                    
                    String customerName = "";
                    if (review.getAppointment() != null && review.getAppointment().getCustomer() != null) {
                        User user = review.getAppointment().getCustomer();
                        if (user.getFirstName() != null && user.getLastName() != null) {
                            customerName = user.getFirstName() + " " + user.getLastName();
                        } else if (user.getUserName() != null) {
                            customerName = user.getUserName();
                        }
                    }
                    boolean matchCustomer = customerName.toLowerCase().contains(kw);
                    
                    matchKeyword = matchDesc || matchCustomer;
                }
                
                boolean matchStatus = true;
                if (query.getStatus() != null && !query.getStatus().isEmpty()) {
                    matchStatus = review.getStatus() != null && 
                                  review.getStatus().name().equalsIgnoreCase(query.getStatus());
                }
                
                return matchKeyword && matchStatus;
            })
            .sorted((r1, r2) -> r2.getCreateAt().compareTo(r1.getCreateAt()))
            .collect(Collectors.toList());

        int total = reviewListFilter.size();
        List<Reviews> pageFilterList = PaginationUtil.paginate(reviewListFilter, query.getPage(), query.getLimit());
        
        List<ReviewResponseDto> items = pageFilterList.stream()
            .map(ReviewMapper::toReviewResponseDto)
            .collect(Collectors.toList());

        return new PageResponse<>(items, total, query.getPage(), query.getLimit());
    }
}
