package com.abs.app.application.staff.blockedslot.command;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.BlockedSlotService;
import com.abs.app.domain.service.StaffAuthorizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateBlockedSlotCommandHandler {
    private final UserRepository userRepository;
    private final BlockedSlotRepository blockedSlotRepository;
    private final BlockedSlotService blockedSlotService;
    private final StaffAuthorizationService staffAuthorizationService;

    public void handle(CreateBlockedSlotCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(user);

        blockedSlotService.validateBlockedSlotRule(
                command.getBlockedDate(),
                command.getStartTime(),
                command.getEndTime());

        if (blockedSlotRepository.existsOverlapping(
                command.getUserId(),
                command.getBlockedDate(),
                command.getStartTime(),
                command.getEndTime(),
                BlockedSlotStatus.REJECTED)) {
            throw new BusinessException(BlockedSlotConstant.OVERLAP_BLOCKED_SLOT);
        }

        BlockedSlot blockedSlot = new BlockedSlot();
        blockedSlot.setStaff(user);
        blockedSlot.setReason(command.getReason());// string , enum
        blockedSlot.setBlockedDate(command.getBlockedDate());
        blockedSlot.setStartTime(command.getStartTime());
        blockedSlot.setEndTime(command.getEndTime());
        blockedSlot.setStatus(BlockedSlotStatus.PENDING);
        blockedSlotRepository.save(blockedSlot);
    }
}
