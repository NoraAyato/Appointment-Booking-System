package com.abs.app.application.admin.staffshift.query;

import com.abs.app.application.admin.staffshift.dto.AdminStaffShiftResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.infrastructure.mapper.StaffShiftMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("adminGetStaffShift")
@RequiredArgsConstructor
public class GetStaffShiftListQueryHandler {
        private final StaffShiftRepository staffShiftRepository;

        @Transactional(readOnly = true)
        public PageResponse<AdminStaffShiftResponseDto> handle(GetStaffShiftListQuery query) {
            Pageable pageable = PaginationUtil.createPageable(
                    query.getPage(),
                    query.getLimit(),
                    Sort.by("workDate").descending().and(Sort.by("startTime").ascending()));
            Optional<StaffShiftStatus> status = EnumUtil.parse(StaffShiftStatus.class, query.getStatus());
            if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
                return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
            }

            Page<StaffShift> staffShifts = staffShiftRepository.search(
                    query.getKeyWord(),
                    status.orElse(null),
                    pageable);

            return PaginationUtil.toPageResponse(
                    staffShifts,
                    StaffShiftMapper::toAdminStaffShiftResponseDto,
                    query.getPage(),
                    query.getLimit());
        }
}
