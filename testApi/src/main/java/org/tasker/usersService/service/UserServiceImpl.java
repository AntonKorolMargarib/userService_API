package org.tasker.usersService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasker.usersService.data.entities.Role;
import org.tasker.usersService.data.entities.User;
import org.tasker.usersService.data.repositories.RoleRepository;
import org.tasker.usersService.data.repositories.UserRepository;
import org.tasker.usersService.web.dto.UserCreateRequest;
import org.tasker.usersService.web.dto.UserResponse;
import org.tasker.usersService.web.dto.UserUpdateRequest;
import org.tasker.usersService.exceptions.NotFoundException;
import org.tasker.usersService.mappers.UserMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;

    private Role ensureRole(Role input) {
        if (input == null || input.getRoleName() == null) return null;
        return roleRepository.findByRoleName(input.getRoleName())
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(input.getRoleName()).build()));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "users", key = "#result.id", condition = "#result != null")
    public UserResponse createUser(UserCreateRequest userRequest) {
        User userEntity = mapper.toEntity(userRequest);
        Role role = ensureRole(mapper.toRole(userRequest.role()));
        userEntity.setRole(role);
        return mapper.toResponse(userRepository.save(userEntity));
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#id")
    public UserResponse getUserById(UUID id) {
        User userEntity = userRepository.findByIdWithRole(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
        return mapper.toResponse(userEntity);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "users", key = "#userUpdateRequest.id")
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        User userEntity = userRepository.findByIdWithRole(userUpdateRequest.id())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userUpdateRequest.id()));

        UUID oldRoleId = userEntity.getRole() != null ? userEntity.getRole().getId() : null;
        mapper.update(userEntity, userUpdateRequest);

        Role role = ensureRole(mapper.toRole(userUpdateRequest.role()));
        userEntity.setRole(role);

        if (oldRoleId != null && userRepository.countByRole_Id(oldRoleId) == 0) {
            roleRepository.deleteById(oldRoleId);
        }
        return mapper.toResponse(userRepository.save(userEntity));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "users", key = "#id")
    public void deleteUser(UUID id) {
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
        UUID roleId = userEntity.getRole() != null ? userEntity.getRole().getId() : null;
        userRepository.delete(userEntity);
        if (roleId != null && userRepository.countByRole_Id(roleId) == 0) {
            roleRepository.deleteById(roleId);
        }
    }
}
