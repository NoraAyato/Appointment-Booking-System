package com.abs.app.application.category.command;

import lombok.Data;

@Data
public class CreateCategoryCommand {
    private final String name;

    private final String description;
}
