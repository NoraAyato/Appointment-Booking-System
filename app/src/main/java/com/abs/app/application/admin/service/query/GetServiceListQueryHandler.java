package com.abs.app.application.admin.service.query;

import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetServiceListQueryHandler {
    private final ServiceRepository serviceRepository;

    public PageResponse<ServiceResponseDto> handle(GetServiceListQuery query) {
        List<ServiceEntity> serviceList = serviceRepository.findAll();
        List<ServiceEntity> serviceListFilter = serviceList.stream()
                .filter(ser -> query.getKeyword() == null || ser.getName().toLowerCase().contains(query.getKeyword()))
                .filter(ser -> query.getStatus() == null
                        || ser.getStatus().toString().equalsIgnoreCase(query.getStatus()))
                .filter(ser -> query.getCategoryId() == null
                        || ser.getCategory().getId().equals(query.getCategoryId()))
                .toList();

        List<ServiceEntity> pageFilterList = PaginationUtil.paginate(serviceListFilter, query.getPage(),
                query.getSize());
        int total = serviceListFilter.size();
        int page = query.getPage();
        int limit = query.getSize();

        List<ServiceResponseDto> items = pageFilterList.stream().map(ServiceMapper::toServiceResponse).toList();

        return new PageResponse<ServiceResponseDto>(items, total, page, limit);
    }
}