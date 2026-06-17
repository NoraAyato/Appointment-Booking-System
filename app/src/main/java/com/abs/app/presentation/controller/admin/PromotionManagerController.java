package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.promotion.command.*;
import com.abs.app.application.admin.promotion.dto.CreatePromotionRequestDto;
import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.application.admin.promotion.dto.UpdatePromotionRequestDto;
import com.abs.app.application.admin.promotion.query.GetPromotionListQuery;
import com.abs.app.application.admin.promotion.query.GetPromotionListQueryHandler;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/promotions")
@RequiredArgsConstructor
@Tag(name = "Admin Promotion Manager")
public class PromotionManagerController {

    private final CreatePromotionCommandHandler createPromotionCommandHandler;
    private final UpdatePromotionCommandHandler updatePromotionCommandHandler;
    private final DeletePromotionCommandHandler deletePromotionCommandHandler;
    private final GetPromotionListQueryHandler getPromotionListQueryHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PromotionResponseDto>>> getPromotions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        GetPromotionListQuery query = new GetPromotionListQuery(page, size, keyword);
        PageResponse<PromotionResponseDto> pageResponse = getPromotionListQueryHandler.handle(query);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thành công", pageResponse));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PromotionResponseDto>> createPromotion(
            @ModelAttribute CreatePromotionRequestDto request) {
        CreatePromotionCommand command = new CreatePromotionCommand(
                request.getDescription(),
                request.getDiscountAmount(),
                request.getDiscountType(),
                request.getActive(),
                request.getImage(),
                request.getStartDate(),
                request.getEndDate()
        );
        PromotionResponseDto responseDto = createPromotionCommandHandler.handle(command);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tạo mới thành công", responseDto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PromotionResponseDto>> updatePromotion(
            @PathVariable String id,
            @ModelAttribute UpdatePromotionRequestDto request) {
        UpdatePromotionCommand command = new UpdatePromotionCommand(
                id,
                request.getDescription(),
                request.getDiscountAmount(),
                request.getDiscountType(),
                request.getActive(),
                request.getImage(),
                request.getStartDate(),
                request.getEndDate()
        );
        PromotionResponseDto responseDto = updatePromotionCommandHandler.handle(command);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật thành công", responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable String id) {
        deletePromotionCommandHandler.handle(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xóa thành công", null));
    }
}
