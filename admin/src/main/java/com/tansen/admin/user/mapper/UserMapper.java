package com.tansen.admin.user.mapper;

import com.tansen.admin.admin.dto.response.ListAdminResponse;
import com.tansen.admin.user.dto.ListUserResponse;
import com.tansen.entity.Admin;
import com.tansen.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    public abstract ListUserResponse entityToResponse(User user);

    public List<ListUserResponse> listAllUsers(List<User> user) {
        return user.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}
