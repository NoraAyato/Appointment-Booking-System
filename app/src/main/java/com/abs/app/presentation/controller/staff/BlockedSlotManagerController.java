package com.abs.app.presentation.controller.staff;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.staff.blockedslot.command.CreateBlockedSlotCommand;
import com.abs.app.application.staff.blockedslot.command.CreateBlockedSlotCommandHandler;
import com.abs.app.application.staff.blockedslot.dto.BlockedSlotResponseDto;
import com.abs.app.application.staff.blockedslot.dto.CreateBlockedSlotRequestDto;
import com.abs.app.application.staff.blockedslot.query.GetStaffBlockedSlotQuery;
import com.abs.app.application.staff.blockedslot.query.GetStaffBlockedSlotQueryHandler;
import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/staff/blocked-slots")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class BlockedSlotManagerController {

        private final GetStaffBlockedSlotQueryHandler getStaffBlockedSlotQueryHandler;
        private final CreateBlockedSlotCommandHandler createBlockedSlotCommandHandler;

        @GetMapping()
        public ResponseEntity<ApiResponse<PageResponse<BlockedSlotResponseDto>>> getStaffBlockedSlots(
                        @RequestParam(required = false) String keyWord,
                        @RequestParam(required = false) String status,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                String userId = SecurityUtils.getCurrentUserId();
                PageResponse<BlockedSlotResponseDto> blockedSlots = getStaffBlockedSlotQueryHandler
                                .handle(new GetStaffBlockedSlotQuery(userId, keyWord, status, page, limit));
                return ResponseEntity
                                .ok(new ApiResponse<>(true, BlockedSlotConstant.GET_STAFF_BLOCKED_SLOTS_SUCCESS,
                                                blockedSlots));
        }

        @PostMapping()
        public ResponseEntity<ApiResponse<Void>> createBlockedSlot(
                        @Valid @RequestBody CreateBlockedSlotRequestDto request) {
                String userId = SecurityUtils.getCurrentUserId();
                createBlockedSlotCommandHandler.handle(new CreateBlockedSlotCommand(
                                userId,
                                request.getReason(),
                                request.getBlockedDate(),
                                request.getStartTime(),
                                request.getEndTime()));
                return ResponseEntity
                                .ok(new ApiResponse<>(true, BlockedSlotConstant.CREATE_BLOCKED_SLOT_SUCCESS, null));
        }
}
