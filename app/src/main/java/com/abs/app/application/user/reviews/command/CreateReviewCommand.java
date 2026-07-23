package com.abs.app.application.user.reviews.command;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewCommand {
    private String customerId;
    private String appointmentId;
    private BigDecimal serviceScore;
    private String description;
    private MultipartFile picture;
}
