package com.abs.app.application.admin.service.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.service.dto.ServiceOptionResponseDto;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.mapper.ServiceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetServiceOptionQueryHandler {
    private final ServiceRepository serviceRepository;

    public List<ServiceOptionResponseDto> handle() {
        return serviceRepository.findAll().stream()
                .map(ServiceMapper::toServiceOptionResponse)
                .toList();
    }
}
