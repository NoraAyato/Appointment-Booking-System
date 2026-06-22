package com.abs.app.application.admin.service.command;

import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.common.constant.CategoryConstant;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.repository.CategoryRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.infrastructure.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateServiceCommandHandler {
    private final ServiceRepository serviceRepository;
    private final FileStorageService fileStorageService;
    private final CategoryRepository categoryRepository;

    public ServiceResponseDto handle(CreateServiceCommand command) {
        if (serviceRepository.existsByNameIgnoreCase(command.getName())) {
            throw new DuplicateResourceException(ServiceEntityConstant.DUPLICATE_RESOURCE);
        }

        Category categoryExist = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CategoryConstant.NOT_EXIST));

        ServiceEntity newService = new ServiceEntity();
        newService.setId(GenerateIdUtil.GenerateId(ServiceEntityConstant.SALT_TAG, ServiceEntityConstant.STRING_LIMIT));
        newService.setName(command.getName());
        newService.setDescription(command.getDescription());
        newService.setDurationMinutes(command.getDurationMinutes());
        newService.setPrice(command.getPrice());
        newService.setCategory(categoryExist);

        if (command.getImages() != null && !command.getImages().isEmpty()) {
            List<ServiceImage> imageList = new ArrayList<>();
            for (int i = 0; i<command.getImages().size(); i++) {
                MultipartFile file = command.getImages().get(i);
                String savePublicPath = fileStorageService.storeService(file, ServiceEntityConstant.SALT_TAG);

                // tạo ServiceImage để lưu vào db
                ServiceImage imageEntity = new ServiceImage();
                imageEntity.setPicture(savePublicPath);
                // set image đầu là isMainImage
                imageEntity.setIsMainImage(i == 0);
                // set quan hệ với Service cha
                imageEntity.setService(newService);
                imageList.add(imageEntity);
            }
            newService.setServiceImage(imageList);
        }
        ServiceEntity saveService = serviceRepository.save(newService);

        return ServiceMapper.toServiceResponse(saveService);
    }
}
