package com.abs.app.application.user.service.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.service.dto.UserServiceResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.service.ServiceBusinessHandle;
import com.abs.app.infrastructure.mapper.ServiceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetServiceDataQueryHandler {
    private final ServiceRepository serviceRepository;
    private final ServiceBusinessHandle serviceBusinessHandle;

    @Transactional(readOnly = true)
    public PageResponse<UserServiceResponseDto> handle(GetServiceDataQuery query) {
        Pageable pageable = PaginationUtil.createPageable(query.getPage(), query.getLimit());
        LocalDateTime requestedStartAt = query.getDate() != null && query.getTime() != null
                ? query.getDate().atTime(query.getTime())
                : null;

        Page<ServiceEntity> services = serviceRepository.searchUserServices(
                query.getKeyWord(),
                query.getCategoryId(),
                query.getDate(),
                query.getTime(),
                requestedStartAt,
                ServiceStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED,
                BlockedSlotStatus.APPROVED,
                AppointmentStatus.CANCELLED,
                pageable);

        List<String> serviceIds = services.getContent().stream()
                .map(ServiceEntity::getId)
                .toList();
        List<ServiceImage> serviceImages = serviceRepository.findImagesByServiceIds(serviceIds);
        Map<String, List<String>> imagesByServiceId = serviceBusinessHandle.getImagesByServiceId(
                serviceIds,
                serviceImages);
        Map<String, Double> ratingsByServiceId = serviceRepository.findAverageRatingsByServiceIds(serviceIds);

        return PaginationUtil.toPageResponse(
                services,
                service -> ServiceMapper.toUserServiceResponse(service, imagesByServiceId, ratingsByServiceId),
                query.getPage(),
                query.getLimit());
    }
}
