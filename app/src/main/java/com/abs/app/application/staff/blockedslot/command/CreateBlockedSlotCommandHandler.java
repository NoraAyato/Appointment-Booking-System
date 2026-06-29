package com.abs.app.application.staff.blockedslot.command;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.BlockedSlotService;
import com.abs.app.domain.service.DateTimeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateBlockedSlotCommandHandler {
    private final UserRepository userRepository;
    private final BlockedSlotRepository blockedSlotRepository;
    private final DateTimeService dateTimeService;
    private final BlockedSlotService blockedSlotService;

    public void handle(CreateBlockedSlotCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (!dateTimeService.isValidTimeRange(command.getStartTime(), command.getEndTime())) {
            throw new BusinessException(Messages.INVALID_TIME);
        }
        if (!dateTimeService.isValidDate(
                command.getBlockedDate())) {
            throw new BusinessException(BlockedSlotConstant.INVALID_DATE);
        }

        if (!blockedSlotService.isValidDateTimeRange(command.getBlockedDate(), command.getStartTime(),
                command.getEndTime(),
                user.getBlockedSlots())) {
            throw new BusinessException(BlockedSlotConstant.INVALID_TIME_RANGE);
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
