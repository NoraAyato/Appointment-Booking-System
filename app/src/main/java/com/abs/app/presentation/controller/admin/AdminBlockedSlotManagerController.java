package com.abs.app.presentation.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.admin.blockedslot.command.CreateBlockedSlotCommand;
import com.abs.app.application.admin.blockedslot.command.CreateBlockedSlotCommandHandler;
import com.abs.app.application.admin.blockedslot.command.DeleteBlockedSlotCommandHandler;
import com.abs.app.application.admin.blockedslot.command.UpdateBlockedSlotCommand;
import com.abs.app.application.admin.blockedslot.command.UpdateBlockedSlotCommandHandler;
import com.abs.app.application.admin.blockedslot.dto.AdminBlockedSlotResponseDto;
import com.abs.app.application.admin.blockedslot.dto.AdminCreateBlockedSlotRequestDto;
import com.abs.app.application.admin.blockedslot.dto.UpdateBlockedSlotRequestDto;
import com.abs.app.application.admin.blockedslot.query.GetBlockedSlotQuery;
import com.abs.app.application.admin.blockedslot.query.GetBlockedSlotQueryHandler;
import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/admin/blocked-slots")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlockedSlotManagerController {
        private final GetBlockedSlotQueryHandler getBlockedSlotQueryHandler;
        private final UpdateBlockedSlotCommandHandler updateBlockedSlotCommandHandler;
        private final CreateBlockedSlotCommandHandler createBlockedSlotCommandHandler;
        private final DeleteBlockedSlotCommandHandler deleteBlockedSlotCommandHandler;

        @GetMapping()
        public ResponseEntity<ApiResponse<PageResponse<AdminBlockedSlotResponseDto>>> getBlockedSlots(
                        @RequestParam(required = false) String keyWord,
                        @RequestParam(required = false) String status,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                PageResponse<AdminBlockedSlotResponseDto> blockedSlots = getBlockedSlotQueryHandler
                                .handle(new GetBlockedSlotQuery(keyWord, status, page, limit));
                return ResponseEntity
                                .ok(new ApiResponse<>(true, BlockedSlotConstant.GET_BLOCKED_SLOTS_SUCCESS,
                                                blockedSlots));
        }

        @PutMapping("update/{id}")
        public ResponseEntity<ApiResponse<Void>> updateBlockedStatus(@PathVariable Long id,
                        @Valid @RequestBody UpdateBlockedSlotRequestDto request) {
                updateBlockedSlotCommandHandler.handle(new UpdateBlockedSlotCommand(id,
                                request.getStatus()));

                return ResponseEntity
                                .ok(new ApiResponse<>(true, BlockedSlotConstant.UPDATE_BLOCKED_SLOT_SUCCESS, null));
        }

        @PostMapping()
        public ResponseEntity<ApiResponse<Void>> createBlockedSlot(
                        @Valid @RequestBody AdminCreateBlockedSlotRequestDto request) {
                createBlockedSlotCommandHandler.handle(new CreateBlockedSlotCommand(
                                request.getUserId(),
                                request.getReason(),
                                request.getBlockedDate(),
                                request.getStartTime(),
                                request.getEndTime(),
                                request.getStatus()));
                return ResponseEntity
                                .ok(new ApiResponse<>(true, BlockedSlotConstant.CREATE_BLOCKED_SLOT_SUCCESS, null));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteBlockedSlot(@PathVariable Long id) {
                deleteBlockedSlotCommandHandler.handle(id);
                return ResponseEntity
                                .ok(new ApiResponse<>(true, BlockedSlotConstant.DELETE_BLOCKED_SLOT_SUCCESS, null));
        }
}
