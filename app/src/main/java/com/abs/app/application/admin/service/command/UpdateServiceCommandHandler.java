package com.abs.app.application.admin.service.command;

import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.service.ServiceBusinessHandle;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.infrastructure.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateServiceCommandHandler {
    private final ServiceRepository serviceRepository;
    private final FileStorageService fileStorageService;
    private final ServiceBusinessHandle serviceBusinessHandle;

    public ServiceResponseDto handle(UpdateServiceCommand command) {
        ServiceEntity serviceEdit = serviceRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ServiceEntityConstant.NOT_EXIST));

        serviceRepository.findByName(command.getName())
                .filter(existing -> !existing.getId().equals(command.getId()))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(ServiceEntityConstant.DUPLICATE_RESOURCE);
                });
        ServiceStatus status = serviceBusinessHandle.handleStatus(command.getStatus());
        serviceEdit.setName(command.getName());
        serviceEdit.setDescription(command.getDescription());
        serviceEdit.setDurationMinutes(command.getDurationMinutes());
        serviceEdit.setPrice(command.getPrice());
        serviceEdit.setStatus(status);

        if (command.getImages() != null && !command.getImages().isEmpty()) {
            List<ServiceImage> imageList = new ArrayList<>();
            for (int i = 0; i < command.getImages().size(); i++) {
                MultipartFile file = command.getImages().get(i);
                String savePublicPath = fileStorageService.storeService(file, ServiceEntityConstant.SALT_TAG);
                // tạo ServiceImage để lưu vào db
                ServiceImage imageEntity = new ServiceImage();
                imageEntity.setPicture(savePublicPath);
                // set image đầu là isMainImage
                imageEntity.setIsMainImage(i == 0);
                // set quan hệ với Service cha
                imageEntity.setService(serviceEdit);
                imageList.add(imageEntity);
            }
            // xóa ảnh cũ trong db
            serviceEdit.getServiceImage().clear();
            serviceEdit.getServiceImage().addAll(imageList);
        }
        ServiceEntity saveService = serviceRepository.save(serviceEdit);

        return ServiceMapper.toServiceResponse(saveService);
    }
}
