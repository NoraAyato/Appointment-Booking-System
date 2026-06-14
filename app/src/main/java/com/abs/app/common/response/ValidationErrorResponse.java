package com.abs.app.common.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {

    private boolean success;
    private String message;
    private Map<String, String> errors;
}