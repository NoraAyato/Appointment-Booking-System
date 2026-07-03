package com.abs.app.application.admin.staffservice.query;

import com.abs.app.application.admin.staffservice.dto.StaffServiceResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.infrastructure.mapper.StaffServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStaffServiceListQueryHandler {
    private final StaffServiceRepository staffServiceRepository;

    public PageResponse<StaffServiceResponseDto> handle(GetStaffServiceListQuery query) {
        List<StaffService> staffServices = staffServiceRepository.findAll();
        List<StaffService> filteredStaffService = staffServices.stream()
                .filter(item -> (query.getKeyword() == null
                || item.getStaff().getFirstName().toLowerCase().contains(query.getKeyword().toLowerCase())
                || item.getStaff().getLastName().toLowerCase().contains(query.getKeyword().toLowerCase()))
                && (query.getStatus() == null || item.getStatus().name().equalsIgnoreCase(query.getStatus())))
                .toList();

        List<StaffService> pageFileterList = PaginationUtil.paginate(filteredStaffService, query.getPage(), query.getLimit());
        List<StaffServiceResponseDto> items = pageFileterList.stream().map(StaffServiceMapper::toStaffServiceResponse).toList();

        int total = filteredStaffService.size();
        int page = query.getPage();
        int limit = query.getLimit();

        return new PageResponse<>(items, total, page, limit);
    }
}
