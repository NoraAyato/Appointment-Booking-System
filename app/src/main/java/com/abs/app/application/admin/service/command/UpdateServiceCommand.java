package com.abs.app.application.admin.service.command;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateServiceCommand {
    private final String id;
    private final String name;
    private final String description;
    private final int durationMinutes;
    private final double price;
    private final List<MultipartFile> images;
}
