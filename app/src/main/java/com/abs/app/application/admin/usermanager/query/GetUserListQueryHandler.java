package com.abs.app.application.admin.usermanager.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.abs.app.application.admin.usermanager.dto.UserResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.EnumUtil;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserListQueryHandler {
    private final UserRepository userRepository;

    public PageResponse<UserResponseDto> handle(GetUserListQuery query) {
        Pageable pageable = PaginationUtil.createPageable(
                query.getPage(),
                query.getLimit(),
                Sort.by("createdAt").descending());
        Optional<RoleEnum> role = EnumUtil.parse(RoleEnum.class, query.getRole());
        Optional<UserStatus> status = EnumUtil.parse(UserStatus.class, query.getStatus());
        if (EnumUtil.isInvalidEnumValue(query.getRole(), role)
                || EnumUtil.isInvalidEnumValue(query.getStatus(), status)) {
            return PaginationUtil.emptyResponse(query.getPage(), query.getLimit());
        }

        Page<User> users = userRepository.findBySearchAndRole(
                query.getSearch(),
                role.orElse(null),
                status.orElse(null),
                pageable);

        return PaginationUtil.toPageResponse(
                users,
                UserMapper::toUserResponseDto,
                query.getPage(),
                query.getLimit());
    }
}
