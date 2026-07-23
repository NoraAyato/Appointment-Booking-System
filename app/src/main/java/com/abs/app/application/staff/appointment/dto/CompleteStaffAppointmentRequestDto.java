package com.abs.app.application.staff.appointment.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompleteStaffAppointmentRequestDto {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    @NotNull(message = "Picture is required")
    private MultipartFile picture;

    @AssertTrue(message = "Picture must not be empty")
    public boolean isPictureNotEmpty() {
        return picture == null || !picture.isEmpty();
    }

    @AssertTrue(message = "Picture must be jpg, jpeg, png, or gif")
    public boolean isPictureExtensionValid() {
        if (picture == null || picture.isEmpty()) {
            return true;
        }

        String originalFilename = picture.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        return extension.equals(".jpg")
                || extension.equals(".jpeg")
                || extension.equals(".png")
                || extension.equals(".gif");
    }

    @AssertTrue(message = "Picture must be 5MB or smaller")
    public boolean isPictureSizeValid() {
        return picture == null || picture.isEmpty() || picture.getSize() <= MAX_IMAGE_SIZE;
    }
}
