package com.abs.app.application.admin.blockedslot.command;

import org.springframework.stereotype.Service;
import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.repository.BlockedSlotRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteBlockedSlotCommandHandler {
    private final BlockedSlotRepository blockedSlotRepository;

    public void handle(Long id) {
        BlockedSlot blockedSlot = blockedSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BlockedSlotConstant.BLOCKED_SLOT_NOT_EXIST));
        blockedSlotRepository.deleteById(blockedSlot.getId());
    }
}
