package com.abs.app.application.staff.blockedslot.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.abs.app.application.staff.blockedslot.dto.BlockedSlotResponseDto;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.mapper.BlockedSlotMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStaffBlockedSlotQueryHandler {
    private final UserRepository userRepository;

    public PageResponse<BlockedSlotResponseDto> handle(GetStaffBlockedSlotQuery query) {
        User user = userRepository.findById(query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        List<BlockedSlot> blockedSlots = user.getBlockedSlots();

        List<BlockedSlot> filteredBlockedSlots = blockedSlots.stream()
                .filter(item -> (query.getKeyWord() == null
                        || item.getReason().toLowerCase().contains(query.getKeyWord().toLowerCase()))
                        && (query.getStatus() == null || item.getStatus().name().equalsIgnoreCase(query.getStatus())))
                .collect(Collectors.toList());
        int total = filteredBlockedSlots.size();
        List<BlockedSlotResponseDto> items = filteredBlockedSlots.stream()
                .map(BlockedSlotMapper::toBlockedSlotResponseDto)
                .collect(Collectors.toList());
        int page = query.getPage();
        int limit = query.getLimit();
        return new PageResponse<>(items, total, page, limit);
    }
}
