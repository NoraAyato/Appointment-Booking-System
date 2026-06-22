package com.abs.app.application.admin.service.command;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateServiceCommand {
    private final String id;
    private final String name;
    private final String description;
    private final Integer durationMinutes;
    private final Double price;
    private final String status;
    private final List<MultipartFile> images;
}
