package com.abs.app.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.ServiceImage;

@Service
public class InvoiceDetailService {
    public String getServiceImage(AppointmentDetail detail, List<ServiceImage> serviceImages) {
        if (detail == null || detail.getService() == null || detail.getService().getId() == null) {
            return null;
        }

        if (serviceImages == null || serviceImages.isEmpty()) {
            return null;
        }

        return serviceImages.stream()
                .filter(image -> image.getPicture() != null)
                .map(ServiceImage::getPicture)
                .findFirst()
                .orElse(null);
    }

    public List<String> getStaffSpecializations(
            AppointmentDetail detail,
            Map<String, List<String>> specializationsByStaffId) {
        if (detail == null || detail.getStaff() == null || detail.getStaff().getUserId() == null) {
            return List.of();
        }

        if (specializationsByStaffId == null || specializationsByStaffId.isEmpty()) {
            return List.of();
        }

        return specializationsByStaffId.getOrDefault(detail.getStaff().getUserId(), List.of());
    }
}
