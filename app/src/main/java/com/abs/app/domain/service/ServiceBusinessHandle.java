package com.abs.app.domain.service;

import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.enums.ServiceStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ServiceBusinessHandle {
    public ServiceStatus handleStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> ServiceStatus.ACTIVE;
            case "INACTIVE" -> ServiceStatus.INACTIVE;
            default -> throw new ResourceNotFoundException(ServiceEntityConstant.STATUS_NOT_EXIST);
        };
    }

    public Map<String, List<String>> getImagesByServiceId(List<String> serviceIds, List<ServiceImage> serviceImages) {
        Map<String, List<String>> imagesByServiceId = new HashMap<>();

        if (serviceIds != null) {
            serviceIds.forEach(serviceId -> imagesByServiceId.put(serviceId, new ArrayList<>()));
        }

        if (serviceImages == null || serviceImages.isEmpty()) {
            return imagesByServiceId;
        }

        for (ServiceImage image : serviceImages) {
            if (image.getService() == null || image.getPicture() == null) {
                continue;
            }
            imagesByServiceId
                    .computeIfAbsent(image.getService().getId(), key -> new ArrayList<>())
                    .add(image.getPicture());
        }

        return imagesByServiceId;
    }
}
