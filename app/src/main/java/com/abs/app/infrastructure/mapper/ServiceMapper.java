package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.service.dto.ServiceImageDto;
import com.abs.app.application.admin.service.dto.ServiceOptionResponseDto;
import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;

import java.util.ArrayList;
import java.util.List;

public class ServiceMapper {
    public static ServiceResponseDto toServiceResponse(ServiceEntity service) {
        ServiceResponseDto dto = new ServiceResponseDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setDurationMinutes(service.getDurationMinutes());
        dto.setPrice(service.getPrice());
        dto.setStatus(service.getStatus().toString());

        if (service.getCategory() != null) {
            dto.setCategoryName(service.getCategory().getName());
        }

        if (service.getServiceImage() != null && !service.getServiceImage().isEmpty()) {
            List<ServiceImageDto> imageDtoList = new ArrayList<>();
            service.getServiceImage().stream().toList().forEach(img -> {
                ServiceImageDto imageDto = new ServiceImageDto(img.getPicture(), img.getIsMainImage());
                imageDtoList.add(imageDto);
            });
            dto.setServiceImageList(imageDtoList);
        }

        return dto;
    }

    public static ServiceOptionResponseDto toServiceOptionResponse(ServiceEntity service) {
        ServiceOptionResponseDto dto = new ServiceOptionResponseDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        return dto;
    }
}
