package com.abs.app.application.admin.service.dto;

import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.ServiceStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ServiceResponseDto {
    private String id;
    private String name;
    private String description;
    private int durationMinutes;
    private double price;
    private String status;
    private String categoryName;
    private List<ServiceImageDto> serviceImageList;
}
