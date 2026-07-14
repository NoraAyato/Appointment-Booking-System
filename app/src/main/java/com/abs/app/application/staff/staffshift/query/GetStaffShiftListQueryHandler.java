package com.abs.app.application.staff.staffshift.query;

import com.abs.app.application.staff.staffshift.dto.StaffShiftResponseDto;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.StaffAuthorizationService;
import com.abs.app.infrastructure.mapper.StaffShiftMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetStaffShiftListQueryHandler {
    private final UserRepository userRepository;
    private final StaffShiftRepository staffShiftRepository;
    private final StaffAuthorizationService staffAuthorizationService;

    @Transactional(readOnly = true)
    public PageResponse<StaffShiftResponseDto> handle(GetStaffShiftListQuery query) {
        User staff = userRepository.findById(query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("workDate").descending().and(Sort.by("startTime").ascending()));
        Optional<StaffShiftStatus> status = EnumUtil.parse(StaffShiftStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        Page<StaffShift> staffShifts = staffShiftRepository.searchByStaffId(
                query.getUserId(),
                query.getKeyWord(),
                status.orElse(null),
                pageable);

        return PaginationUtil.toPageResponse(
                staffShifts,
                StaffShiftMapper::toStaffShiftResponseDto,
                query.getPage(),
                query.getLimit());
    }
}
