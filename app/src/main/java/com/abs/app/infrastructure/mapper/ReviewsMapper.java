package com.abs.app.infrastructure.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.abs.app.application.admin.reviews.dto.ReviewsResponseDto;
import com.abs.app.application.user.reviews.dto.RatingDistribution;
import com.abs.app.application.user.reviews.dto.ServiceReviewsResponseDto;
import com.abs.app.application.user.reviews.dto.ServiceReviewsStatsResponseDto;
import com.abs.app.application.user.reviews.dto.TopReviewsResponseDto;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.User;

public class ReviewsMapper {
    public static ReviewsResponseDto toAdminReviewResponseDto(Reviews reviews) {
        ReviewsResponseDto responseDto = new ReviewsResponseDto();
        responseDto.setId(reviews.getId());
        responseDto.setPicture(reviews.getPicture());
        responseDto.setDescription(reviews.getDescription());
        responseDto.setServiceScore(reviews.getServiceScore());
        responseDto.setCreateAt(reviews.getCreateAt());
        responseDto.setStatus(reviews.getStatus());
        java.lang.String customerName = reviews.getAppointment().getCustomer().getFirstName() + " "
                + reviews.getAppointment().getCustomer().getLastName();
        responseDto.setCustomerName(customerName);
        return responseDto;
    }

    public static ServiceReviewsResponseDto toServiceReviewsResponseDto(Reviews reviews, String serviceId) {
        ServiceReviewsResponseDto responseDto = new ServiceReviewsResponseDto();
        responseDto.setId(reviews.getId());
        responseDto.setImageUrl(reviews.getPicture());
        responseDto.setContent(reviews.getDescription());
        responseDto.setRating(reviews.getServiceScore() != null ? reviews.getServiceScore().doubleValue() : 0D);
        responseDto.setCreatedAt(reviews.getCreateAt() != null ? reviews.getCreateAt().toLocalDate() : null);

        if (reviews.getAppointment() == null) {
            return responseDto;
        }

        User customer = reviews.getAppointment().getCustomer();
        if (customer != null) {
            responseDto.setCustomerName(toFullName(customer));
            responseDto.setCustomerAvatar(customer.getPicture());
        }

        if (reviews.getAppointment().getAppointmentDetails() != null) {
            AppointmentDetail serviceDetail = reviews.getAppointment().getAppointmentDetails().stream()
                    .filter(detail -> detail.getService() != null && serviceId.equals(detail.getService().getId()))
                    .findFirst()
                    .orElse(null);
            if (serviceDetail != null) {
                User staff = serviceDetail.getStaff();
                if (staff != null) {
                    responseDto.setStaffName(toFullName(staff));
                }
                LocalDate serviceDate = serviceDetail.getStartTime() != null
                        ? serviceDetail.getStartTime().toLocalDate()
                        : null;
                responseDto.setServiceDate(serviceDate);
            }
        }

        return responseDto;
    }

    public static ServiceReviewsStatsResponseDto toServiceReviewsStatsResponseDto(
            double averageRating,
            long totalReviews,
            List<Object[]> ratingRows) {
        return new ServiceReviewsStatsResponseDto(
                averageRating,
                totalReviews,
                toRatingDistribution(ratingRows, totalReviews));
    }

    public static TopReviewsResponseDto toTopReviewsResponseDto(Reviews reviews) {
        TopReviewsResponseDto responseDto = new TopReviewsResponseDto();
        responseDto.setId(reviews.getId());
        responseDto.setImageUrl(reviews.getPicture());
        responseDto.setContent(reviews.getDescription());
        responseDto.setRating(reviews.getServiceScore() != null ? reviews.getServiceScore().doubleValue() : 0D);

        if (reviews.getAppointment() == null) {
            return responseDto;
        }

        User customer = reviews.getAppointment().getCustomer();
        if (customer != null) {
            responseDto.setCustomerName(toFullName(customer));
            responseDto.setCustomerAvatar(customer.getPicture());
        }

        if (reviews.getAppointment().getAppointmentDetails() != null) {
            reviews.getAppointment().getAppointmentDetails().stream()
                    .filter(detail -> detail.getStartTime() != null)
                    .findFirst()
                    .ifPresent(detail -> {
                        if (detail.getService() != null) {
                            responseDto.setServiceName(detail.getService().getName());
                        }
                        responseDto.setServiceDate(detail.getStartTime().toLocalDate().toString());
                    });
        }

        return responseDto;
    }

    private static List<RatingDistribution> toRatingDistribution(List<Object[]> ratingRows, long totalReviews) {
        List<Object[]> rows = ratingRows != null ? ratingRows : List.of();
        Map<Integer, Long> countsByRating = rows.stream()
                .collect(Collectors.toMap(
                        row -> normalizeRating(((Number) row[0]).intValue()),
                        row -> ((Number) row[1]).longValue(),
                        Long::sum));
        List<RatingDistribution> distribution = new ArrayList<>();

        for (int rating = 5; rating >= 1; rating--) {
            long count = countsByRating.getOrDefault(rating, 0L);
            double percentage = totalReviews > 0 ? count * 100D / totalReviews : 0D;
            distribution.add(new RatingDistribution(rating, (int) count, percentage));
        }

        return distribution;
    }

    private static int normalizeRating(int rating) {
        if (rating < 1) {
            return 1;
        }
        if (rating > 5) {
            return 5;
        }
        return rating;
    }

    private static String toFullName(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
