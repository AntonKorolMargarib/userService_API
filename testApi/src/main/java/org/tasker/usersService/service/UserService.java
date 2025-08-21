package org.tasker.usersService.service;


import org.tasker.usersService.web.dto.UserCreateRequest;
import org.tasker.usersService.web.dto.UserResponse;
import org.tasker.usersService.web.dto.UserUpdateRequest;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserById(UUID id);
    UserResponse updateUser(UserUpdateRequest request);
    void deleteUser(UUID id);
}
