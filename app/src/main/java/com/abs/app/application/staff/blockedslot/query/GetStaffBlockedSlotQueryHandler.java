package com.abs.app.application.staff.blockedslot.query;

import com.abs.app.application.staff.blockedslot.dto.BlockedSlotResponseDto;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.StaffAuthorizationService;
import com.abs.app.infrastructure.mapper.BlockedSlotMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetStaffBlockedSlotQueryHandler {
    private final UserRepository userRepository;
    private final BlockedSlotRepository blockedSlotRepository;
    private final StaffAuthorizationService staffAuthorizationService;

    public PageResponse<BlockedSlotResponseDto> handle(GetStaffBlockedSlotQuery query) {
        User staff = userRepository.findById(query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("blockedDate").descending().and(Sort.by("startTime").ascending()));
        Optional<BlockedSlotStatus> status = EnumUtil.parse(BlockedSlotStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        Page<BlockedSlot> blockedSlots = blockedSlotRepository.searchByStaffId(
                query.getUserId(),
                query.getKeyWord(),
                status.orElse(null),
                pageable);

        return PaginationUtil.toPageResponse(
                blockedSlots,
                BlockedSlotMapper::toBlockedSlotResponseDto,
                query.getPage(),
                query.getLimit());
    }
}
