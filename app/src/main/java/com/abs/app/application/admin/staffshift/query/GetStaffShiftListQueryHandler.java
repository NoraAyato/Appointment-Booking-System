package com.abs.app.application.admin.staffshift.query;

import com.abs.app.application.admin.staffshift.dto.AdminStaffShiftResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.repository.StaffShiftRepository;
import com.abs.app.infrastructure.mapper.StaffShiftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminGetStaffShift")
@RequiredArgsConstructor
public class GetStaffShiftListQueryHandler {
        private final StaffShiftRepository staffShiftRepository;

        public PageResponse<AdminStaffShiftResponseDto> handle(GetStaffShiftListQuery query) {
            List<StaffShift> staffShifts = staffShiftRepository.findAll();
            List<StaffShift> filteredShifts = staffShifts.stream()
                    .filter(item -> {
                        boolean matchKeyword = query.getKeyWord() == null || query.getKeyWord().isBlank() ||
                                (item.getStaff().getFirstName() != null && item.getStaff().getFirstName().toLowerCase().contains(query.getKeyWord().toLowerCase())) ||
                                (item.getStaff().getLastName() != null && item.getStaff().getLastName().toLowerCase().contains(query.getKeyWord().toLowerCase()));

                        boolean matchStatus = query.getStatus() == null || query.getStatus().isBlank() ||
                                item.getStatus().name().equalsIgnoreCase(query.getStatus());

                        return matchKeyword && matchStatus;
                    })
                    .toList();

            List<StaffShift> pageFilterList = PaginationUtil.paginate(filteredShifts, query.getPage(), query.getLimit());

            List<AdminStaffShiftResponseDto> items = pageFilterList.stream()
                    .map(StaffShiftMapper::toAdminStaffShiftResponseDto)
                    .toList();

            int total = filteredShifts.size();
            int page = query.getPage();
            int limit = query.getLimit();

            return new PageResponse<>(items, total, page, limit);
        }
}
