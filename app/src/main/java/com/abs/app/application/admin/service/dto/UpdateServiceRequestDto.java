package com.abs.app.application.admin.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateServiceRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    private String description;

    @Min(value = 5)
    @Max(value = 480)
    private int durationMinutes;

    @Min(value = 0)
    private double price;

    private String categoryName;

    @Size(max = 5)
    private List<MultipartFile> images;
}
