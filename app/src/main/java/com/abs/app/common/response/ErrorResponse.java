package com.abs.app.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private boolean success;
    private String message;
}