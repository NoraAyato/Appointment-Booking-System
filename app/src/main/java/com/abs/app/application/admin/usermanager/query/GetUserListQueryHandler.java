package com.abs.app.application.admin.usermanager.query;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.abs.app.application.admin.usermanager.dto.UserResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserListQueryHandler {
    private final UserRepository userRepository;

    public PageResponse<UserResponseDto> handle(GetUserListQuery query) {
        List<User> userListFiltered = userRepository.findAll().stream()
                .filter(user -> (query.getRole() == null
                        || user.getRole().getRoleName().name().equals(query.getRole())))
                .filter(user -> (query.getStatus() == null || user.getStatus().name().equals(query.getStatus())))
                .filter(user -> (query.getSearch() == null
                        || user.getUserName().toLowerCase().contains(query.getSearch().toLowerCase())
                        || user.getEmail().toLowerCase().contains(query.getSearch().toLowerCase())
                        || user.getPhoneNumber().toLowerCase().contains(query.getSearch().toLowerCase())))
                .toList();
        List<User> paginateList = PaginationUtil.paginate(userListFiltered, query.getPage(), query.getLimit());
        List<UserResponseDto> userDtoList = paginateList.stream()
                .map(UserMapper::toUserResponseDto)
                .toList();
        int total = userListFiltered.size();
        int page = query.getPage();
        int limit = query.getLimit();
        return new PageResponse<>(userDtoList, total, page, limit);
    }
}
