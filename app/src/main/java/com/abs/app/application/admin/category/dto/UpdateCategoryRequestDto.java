package com.abs.app.application.admin.category.dto;

import com.abs.app.common.constant.CategoryConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequestDto {

    @NotBlank(message = CategoryConstant.VALID_NAME_NOT_BLANK)
    @Size(min = 1, max = 100, message = CategoryConstant.VALID_NAME_SIZE)
    private String name;

    @NotBlank(message = CategoryConstant.VALID_DESCRIPTION_NOT_BLANK)
    private String description;
}
