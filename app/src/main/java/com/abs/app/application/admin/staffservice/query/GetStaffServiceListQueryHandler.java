package com.abs.app.application.admin.staffservice.query;

import com.abs.app.application.admin.staffservice.dto.StaffServiceResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.infrastructure.mapper.StaffServiceMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetStaffServiceListQueryHandler {
    private final StaffServiceRepository staffServiceRepository;

    public PageResponse<StaffServiceResponseDto> handle(GetStaffServiceListQuery query) {
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("id").descending());
        Optional<StaffServiceStatus> status = EnumUtil.parse(StaffServiceStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        Page<StaffService> staffServices = staffServiceRepository.search(
                query.getKeyword(),
                status.orElse(null),
                pageable);

        return PaginationUtil.toPageResponse(
                staffServices,
                StaffServiceMapper::toStaffServiceResponse,
                query.getPage(),
                query.getLimit());
    }
}
