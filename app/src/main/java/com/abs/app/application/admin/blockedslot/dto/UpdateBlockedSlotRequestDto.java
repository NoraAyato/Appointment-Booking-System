package com.abs.app.application.admin.blockedslot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBlockedSlotRequestDto {
    @NotBlank(message = "Status is required")
    @NotNull
    private String status;
}
