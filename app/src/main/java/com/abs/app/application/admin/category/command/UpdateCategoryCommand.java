package com.abs.app.application.admin.category.command;

import lombok.Data;

@Data
public class UpdateCategoryCommand {
    private final String id;
    private final String tagColor;
    private final String name;
    private final String description;
}
