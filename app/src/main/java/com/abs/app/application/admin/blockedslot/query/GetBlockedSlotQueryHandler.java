package com.abs.app.application.admin.blockedslot.query;

import com.abs.app.application.admin.blockedslot.dto.AdminBlockedSlotResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.infrastructure.mapper.BlockedSlotMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetBlockedSlotQueryHandler {
        private final BlockedSlotRepository blockedSlotRepository;

        public PageResponse<AdminBlockedSlotResponseDto> handle(GetBlockedSlotQuery query) {
                Pageable pageable = PaginationUtil.createPageable(
                                query.getPage(),
                                query.getLimit(),
                                Sort.by("blockedDate").descending().and(Sort.by("startTime").ascending()));
                Optional<BlockedSlotStatus> status = EnumUtil.parse(BlockedSlotStatus.class, query.getStatus());
                if (EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
                        return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
                }

                Page<BlockedSlot> blockedSlots = blockedSlotRepository.search(
                                query.getKeyWord(),
                                status.orElse(null),
                                pageable);

                return PaginationUtil.toPageResponse(
                                blockedSlots,
                                BlockedSlotMapper::toAdminBlockedSlotResponseDto,
                                query.getPage(),
                                query.getLimit());
        }
}
