package com.abs.app.application.admin.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @NotNull
    private String description;
}
