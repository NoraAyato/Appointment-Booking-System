package com.abs.app.application.staff.staffshift.query;

import com.abs.app.application.staff.staffshift.dto.StaffShiftResponseDto;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.mapper.StaffShiftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStaffShiftListQueryHandler {
    private final UserRepository userRepository;

    public PageResponse<StaffShiftResponseDto> handle(GetStaffShiftListQuery query) {
        User staff = userRepository.findById(query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (!staff.getRole().getRoleName().equals(RoleEnum.STAFF))
            throw new BusinessException(StaffShiftConstant.ONLY_STAFF_ALLOWED);

        List<StaffShift> staffShift = staff.getStaffShifts();

        List<StaffShift> filteredStaffShift = staffShift.stream()
                .filter(item -> query.getKeyWord() == null
                        && (query.getStatus() == null || item.getStatus().name().equalsIgnoreCase(query.getStatus())))
                .toList();

        List<StaffShift> pageFilterList = PaginationUtil.paginate(filteredStaffShift, query.getPage(), query.getLimit());
        List<StaffShiftResponseDto> items = pageFilterList.stream()
                .map(StaffShiftMapper::toStaffShiftResponseDto)
                .toList();

        int total = filteredStaffShift.size();
        int page = query.getPage();
        int limit = query.getLimit();

        return new PageResponse<>(items, total, page, limit);
    }
}
