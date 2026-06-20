package com.abs.app.application.admin.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceImageDto {

    private String picture;

    private Boolean isMainImage;
}
