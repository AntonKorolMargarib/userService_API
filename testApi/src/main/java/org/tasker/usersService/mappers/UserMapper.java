package org.tasker.usersService.mappers;

import org.mapstruct.*;
import org.tasker.usersService.data.entities.Role;
import org.tasker.usersService.data.entities.User;
import org.tasker.usersService.web.dto.RoleDto;
import org.tasker.usersService.web.dto.UserCreateRequest;
import org.tasker.usersService.web.dto.UserResponse;
import org.tasker.usersService.web.dto.UserUpdateRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserCreateRequest req);

    @Mapping(target = "role", ignore = true)
    void update(@MappingTarget User entity, UserUpdateRequest dto);

    default Role toRole(RoleDto dto) {
        if (dto == null) return null;
        return Role.builder().roleName(dto.roleName()).build();
    }

    default UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFio(),
                user.getPhoneNumber(),
                user.getAvatar(),
                user.getRole() != null ? user.getRole().getRoleName() : null
        );
    }
}