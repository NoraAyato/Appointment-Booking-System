package com.abs.app.application.admin.staffservice.command;

import com.abs.app.common.constant.RoleConstant;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.constant.StaffServiceConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.repository.RoleRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateStaffServiceCommandHandler {
    private final StaffServiceRepository staffServiceRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public void handle(CreateStaffServiceCommand command) {
        User staff = userRepository.findById(command.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (!staff.getRole().getRoleName().equals(RoleEnum.STAFF)) {
            throw new BusinessException(StaffServiceConstant.USER_NOT_STAFF);
        }

        Optional<StaffService> existingStaffService = staffServiceRepository.findByStaffUserIdAndServiceId(command.getStaffId(), command.getServiceId());

        if (existingStaffService.isPresent()) {
            StaffService staffService = existingStaffService.get();
            if (staffService.getStatus() == StaffServiceStatus.ACTIVE) {
                throw new BusinessException(StaffServiceConstant.STAFF_ALREADY_RESPONSIBLE_SERVICE);
            } else {
                staffService.setStatus(StaffServiceStatus.ACTIVE);
                staffServiceRepository.save(staffService);
            }
        } else {
            ServiceEntity serviceEntity = serviceRepository.findById(command.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException(ServiceEntityConstant.NOT_EXIST));

            StaffService newStaffService = new StaffService();
            newStaffService.setStaff(staff);
            newStaffService.setService(serviceEntity);
            staffServiceRepository.save(newStaffService);
        }
    }
}
