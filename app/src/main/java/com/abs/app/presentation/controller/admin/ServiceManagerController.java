package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.service.command.CreateServiceCommand;
import com.abs.app.application.admin.service.command.CreateServiceCommandHandler;
import com.abs.app.application.admin.service.command.UpdateServiceCommand;
import com.abs.app.application.admin.service.command.UpdateServiceCommandHandler;
import com.abs.app.application.admin.service.dto.CreateServiceRequestDto;
import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.application.admin.service.dto.UpdateServiceRequestDto;
import com.abs.app.application.admin.service.query.GetServiceListQuery;
import com.abs.app.application.admin.service.query.GetServiceListQueryHandler;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class ServiceManagerController {
    private final GetServiceListQueryHandler getServiceListQueryHandler;
    private final CreateServiceCommandHandler createServiceCommandHandler;
    private final UpdateServiceCommandHandler updateServiceCommandHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ServiceResponseDto>>> getServices(@RequestParam(required = false) String keyword,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "5") int size) {
        PageResponse<ServiceResponseDto> pageResponse = getServiceListQueryHandler.handle(new GetServiceListQuery(keyword, page, size));
        return ResponseEntity.ok(new ApiResponse<>(true, ServiceEntityConstant.GET_SUCCESS, pageResponse));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ServiceResponseDto>> create(@Valid @ModelAttribute CreateServiceRequestDto request) {
        CreateServiceCommand command = new CreateServiceCommand(
                request.getName(),
                request.getDescription(),
                request.getDurationMinutes(),
                request.getPrice(),
                request.getCategoryId(),
                request.getImages()
        );
        ServiceResponseDto responseDto = createServiceCommandHandler.handle(command);
        return ResponseEntity.ok(new ApiResponse<>(true, ServiceEntityConstant.CREATE_SUCCESS, responseDto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ServiceResponseDto>> update(@PathVariable String id, @Valid @ModelAttribute UpdateServiceRequestDto request) {
        UpdateServiceCommand command = new UpdateServiceCommand(
                id,
                request.getName(),
                request.getDescription(),
                request.getDurationMinutes(),
                request.getPrice(),
                request.getStatus(),
                request.getImages()
        );
        ServiceResponseDto responseDto = updateServiceCommandHandler.handle(command);

        return ResponseEntity.ok(new ApiResponse<>(true, ServiceEntityConstant.UPDATE_SUCCESS, responseDto));
    }
}
