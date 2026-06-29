package com.abs.app.application.admin.blockedslot.command;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.service.BlockedSlotService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateBlockedSlotCommandHandler {
    private final BlockedSlotRepository blockedSlotRepository;
    private final BlockedSlotService blockedSlotService;

    public void handle(UpdateBlockedSlotCommand command) {
        BlockedSlot blockedSlot = blockedSlotRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(BlockedSlotConstant.BLOCKED_SLOT_NOT_EXIST));
        BlockedSlotStatus newStatus = blockedSlotService.handleBlockedStatus(command.getStatus());
        blockedSlot.setStatus(newStatus);
        blockedSlotRepository.save(blockedSlot);
    }
}
