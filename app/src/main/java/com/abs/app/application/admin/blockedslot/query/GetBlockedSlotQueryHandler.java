package com.abs.app.application.admin.blockedslot.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.blockedslot.dto.AdminBlockedSlotResponseDto;
import com.abs.app.application.staff.blockedslot.dto.BlockedSlotResponseDto;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.mapper.BlockedSlotMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetBlockedSlotQueryHandler {
        private final BlockedSlotRepository blockedSlotRepository;

        public PageResponse<AdminBlockedSlotResponseDto> handle(GetBlockedSlotQuery query) {

                List<BlockedSlot> blockedSlots = blockedSlotRepository.findAll();

                List<BlockedSlot> filteredBlockedSlots = blockedSlots.stream()
                                .filter(item -> (query.getKeyWord() == null
                                                || item.getReason().toLowerCase()
                                                                .contains(query.getKeyWord().toLowerCase())
                                                || item.getStaff().getFirstName().toLowerCase()
                                                                .contains(query.getKeyWord().toLowerCase())
                                                || item.getStaff().getLastName().toLowerCase()
                                                                .contains(query.getKeyWord().toLowerCase()))
                                                && (query.getStatus() == null || item.getStatus().name()
                                                                .equalsIgnoreCase(query.getStatus())))
                                .collect(Collectors.toList());
                int total = filteredBlockedSlots.size();
                List<AdminBlockedSlotResponseDto> items = filteredBlockedSlots.stream()
                                .map(BlockedSlotMapper::toAdminBlockedSlotResponseDto)
                                .collect(Collectors.toList());
                int page = query.getPage();
                int limit = query.getLimit();
                return new PageResponse<>(items, total, page, limit);
        }
}
