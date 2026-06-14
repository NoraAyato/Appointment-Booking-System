package com.abs.app.application.admin.category.command;

import lombok.Data;

@Data
public class CreateCategoryCommand {
    private final String name;

    private final String description;
}
