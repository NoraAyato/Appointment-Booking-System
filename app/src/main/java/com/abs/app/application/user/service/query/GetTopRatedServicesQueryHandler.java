package com.abs.app.application.user.service.query;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.service.dto.UserServiceResponseDto;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.service.ServiceBusinessHandle;
import com.abs.app.infrastructure.mapper.ServiceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTopRatedServicesQueryHandler {
        private static final int TOP_RATED_LIMIT = 3;

        private final ServiceRepository serviceRepository;
        private final ServiceBusinessHandle serviceBusinessHandle;

        @Transactional(readOnly = true)
        public List<UserServiceResponseDto> handle() {
                List<ServiceEntity> services = serviceRepository.findTopRatedServices(
                                ServiceStatus.ACTIVE,
                                ReviewsStatus.APPROVED,
                                TOP_RATED_LIMIT);
                List<String> serviceIds = services.stream()
                                .map(ServiceEntity::getId)
                                .toList();
                List<ServiceImage> serviceImages = serviceRepository.findImagesByServiceIds(serviceIds);
                Map<String, List<String>> imagesByServiceId = serviceBusinessHandle.getImagesByServiceId(
                                serviceIds,
                                serviceImages);
                Map<String, Double> ratingsByServiceId = serviceRepository.findAverageRatingsByServiceIds(serviceIds);

                return services.stream()
                                .map(service -> ServiceMapper.toUserServiceResponse(service, imagesByServiceId,
                                                ratingsByServiceId))
                                .toList();
        }
}
