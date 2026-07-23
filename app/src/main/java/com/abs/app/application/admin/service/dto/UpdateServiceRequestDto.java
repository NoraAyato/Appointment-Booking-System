package com.abs.app.application.admin.service.dto;

import com.abs.app.common.constant.ServiceEntityConstant;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateServiceRequestDto {

    @NotBlank(message = ServiceEntityConstant.VALID_NAME_NOT_BLANK)
    @Size(min = 1, max = 100, message = ServiceEntityConstant.VALID_NAME_SIZE)
    private String name;

    @NotBlank(message = ServiceEntityConstant.VALID_DESCRIPTION_NOT_BLANK)
    private String description;

    @NotNull(message = ServiceEntityConstant.VALID_DURATION_NOT_NULL)
    @Min(value = 5, message = ServiceEntityConstant.VALID_DURATION_MIN)
    @Max(value = 480, message = ServiceEntityConstant.VALID_DURATION_MAX)
    private Integer durationMinutes;

    @NotNull(message = ServiceEntityConstant.VALID_PRICE_NOT_NULL)
    @Min(value = 0, message = ServiceEntityConstant.VALID_PRICE_MIN)
    private Double price;

    @NotBlank(message = ServiceEntityConstant.VALID_STATUS_NOT_BLANK)
    private String status;

    @NotBlank(message = ServiceEntityConstant.VALID_CATEGORY_NOT_BLANK)
    private String categoryName;

    @Size(max = 5, message = ServiceEntityConstant.VALID_IMAGES_SIZE)
    private List<MultipartFile> images;
}
