package com.abs.app.application.admin.staffservice.dto;

import com.abs.app.common.constant.StaffServiceConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateStaffServiceRequestDto {

    @NotBlank(message = StaffServiceConstant.VALID_STAFF_ID_REQUIRED)
    private String staffId;

    @NotBlank(message = StaffServiceConstant.VALID_SERVICE_ID_REQUIRED)
    private String serviceId;
}
