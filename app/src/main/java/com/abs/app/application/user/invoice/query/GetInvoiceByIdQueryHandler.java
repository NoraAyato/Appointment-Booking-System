package com.abs.app.application.user.invoice.query;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.invoice.dto.InvoiceResponseDto;
import com.abs.app.common.constant.InvoiceConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.service.InvoiceDetailService;
import com.abs.app.domain.service.PromotionService;
import com.abs.app.infrastructure.mapper.InvoiceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetInvoiceByIdQueryHandler {
    private final InvoiceRepository invoiceRepository;
    private final ServiceRepository serviceRepository;
    private final StaffServiceRepository staffServiceRepository;
    private final InvoiceDetailService invoiceDetailService;
    private final PromotionService promotionService;

    @Transactional(readOnly = true)
    public InvoiceResponseDto handle(GetInvoiceByIdQuery query) {
        Invoice invoice = invoiceRepository.findByIdAndCustomerId(
                query.getInvoiceId(),
                query.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceConstant.NOT_FOUND));

        AppointmentDetail detail = invoice.getAppointment().getAppointmentDetails().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceConstant.NOT_FOUND));

        List<ServiceImage> serviceImages = List.of();
        if (detail.getService() != null && detail.getService().getId() != null) {
            serviceImages = serviceRepository.findImagesByServiceIds(List.of(detail.getService().getId()));
        }

        Map<String, List<String>> specializationsByStaffId = Map.of();
        if (detail.getStaff() != null && detail.getStaff().getUserId() != null) {
            specializationsByStaffId = staffServiceRepository.findSpecialtiesByStaffIds(
                    List.of(detail.getStaff().getUserId()),
                    StaffServiceStatus.ACTIVE,
                    ServiceStatus.ACTIVE);
        }

        String serviceImage = invoiceDetailService.getServiceImage(detail, serviceImages);
        List<String> staffSpecializations = invoiceDetailService.getStaffSpecializations(
                detail,
                specializationsByStaffId);
        String discountValue = promotionService.calculateInvoiceDiscountValue(invoice);

        return InvoiceMapper.toInvoiceResponse(invoice, detail, serviceImage, staffSpecializations, discountValue);
    }
}
