package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.promotion.command.CreatePromotionCommand;
import com.abs.app.application.admin.promotion.command.CreatePromotionCommandHandler;
import com.abs.app.application.admin.promotion.command.DeletePromotionCommandHandler;
import com.abs.app.application.admin.promotion.command.UpdatePromotionCommand;
import com.abs.app.application.admin.promotion.command.UpdatePromotionCommandHandler;
import com.abs.app.application.admin.promotion.dto.CreatePromotionRequestDto;
import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.application.admin.promotion.dto.UpdatePromotionRequestDto;
import com.abs.app.application.admin.promotion.query.GetPromotionListQuery;
import com.abs.app.application.admin.promotion.query.GetPromotionListQueryHandler;
import com.abs.app.common.constant.PromotionConstant;
import java.time.LocalDate;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import com.abs.app.infrastructure.file.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class PromotionManagerController {
    private final GetPromotionListQueryHandler getPromotionListQueryHandler;
    private final CreatePromotionCommandHandler createPromotionCommandHandler;
    private final UpdatePromotionCommandHandler updatePromotionCommandHandler;
    private final DeletePromotionCommandHandler deletePromotionCommandHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PromotionResponseDto>>> getPromotions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<PromotionResponseDto> pageResponse = getPromotionListQueryHandler.handle(
                new GetPromotionListQuery(keyword, active, fromDate, toDate, page, size));
        return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstant.GET_SUCCESS, pageResponse));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PromotionResponseDto>> create(@Valid @ModelAttribute CreatePromotionRequestDto request) {
        PromotionResponseDto responseDto = createPromotionCommandHandler.handle(
                new CreatePromotionCommand(
                        request.getDescription(),
                        request.getDiscountAmount(),
                        request.getDiscountType(),
                        request.getActive(),
                        request.getImage(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getUserId()));
        return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstant.CREATE_SUCCESS, responseDto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PromotionResponseDto>> update(
            @PathVariable String id,
            @Valid @ModelAttribute UpdatePromotionRequestDto request) {
        PromotionResponseDto responseDto = updatePromotionCommandHandler.handle(
                new UpdatePromotionCommand(
                        id,
                        request.getDescription(),
                        request.getDiscountAmount(),
                        request.getDiscountType(),
                        request.getActive(),
                        request.getImage(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getUserId()));
        return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstant.UPDATE_SUCCESS, responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable String id) {
        deletePromotionCommandHandler.handle(id);
        return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstant.DELETE_SUCCESS, null));
    }
}
