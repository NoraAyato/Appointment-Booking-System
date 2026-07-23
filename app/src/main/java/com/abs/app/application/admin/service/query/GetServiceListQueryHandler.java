package com.abs.app.application.admin.service.query;

import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetServiceListQueryHandler {
    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public PageResponse<ServiceResponseDto> handle(GetServiceListQuery query) {
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getSize(),
                Sort.by("name").ascending());
        Optional<ServiceStatus> status = EnumUtil.parse(ServiceStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getSize());
        }

        Page<com.abs.app.domain.entity.ServiceEntity> services = serviceRepository.search(
                query.getKeyword(),
                status.orElse(null),
                query.getCategoryId(),
                pageable);

        return PaginationUtil.toPageResponse(
                services,
                ServiceMapper::toServiceResponse,
                query.getPage(),
                query.getSize());
    }
}
