package com.abs.app.application.admin.staffservice.command;

import com.abs.app.common.constant.StaffServiceConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.repository.StaffServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteStaffServiceCommandHandler {
    private final StaffServiceRepository staffServiceRepository;

    public void handle(Long id) {

        StaffService staffService = staffServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StaffServiceConstant.STAFF_NOT_ASSIGNED_SERVICE));

//        StaffService staffService = staffServiceRepository.findByStaffUserIdAndServiceId(command.getStaffId(), command.getServiceId())
//                .orElseThrow(() -> new ResourceNotFoundException(StaffServiceConstant.STAFF_NOT_ASSIGNED_SERVICE));

        if (staffService.getStatus() == StaffServiceStatus.BLOCKED) {
            throw new BusinessException(StaffServiceConstant.SERVICE_ALREADY_DELETED_FROM_STAFF);
        }

        staffService.setStatus(StaffServiceStatus.BLOCKED);
        staffServiceRepository.save(staffService);
    }
}
