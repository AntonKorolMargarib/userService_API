package org.tasker.usersService.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tasker.usersService.web.dto.UserCreateRequest;
import org.tasker.usersService.web.dto.UserResponse;
import org.tasker.usersService.web.dto.UserUpdateRequest;
import org.tasker.usersService.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/createNewUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRequest.class),
                            examples = @ExampleObject(value = "{\"fio\":\"Иванов Иван Иванович\",\"phoneNumber\":\"+79990000001\",\"avatar\":\"https://example.com/avatar.png\",\"role\":{\"roleName\":\"USER\"}}"))
            )
    )
    @ApiResponse(responseCode = "201", description = "Создано", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    public UserResponse createNewUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }


    @GetMapping("/users/{id}")
    @Operation(summary = "Получить пользователя по id")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    public UserResponse getById(@Parameter(example = "11111111-1111-1111-1111-111111111111") @PathVariable UUID id) {
        return userService.getUserById(id);
    }


//  Совместимость: query param userID
    @GetMapping(value = "/users", params = "userID")
    @Operation(summary = "Получить пользователя по userID (query)")
    public UserResponse getByQuery(@RequestParam("userID") UUID id) {
        return userService.getUserById(id);
    }


    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновить пользователя по id")
    @ApiResponse(responseCode = "200", description = "Данные обновлены")
    public UserResponse updateById(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        if (!id.equals(request.id())) throw new IllegalArgumentException("id path != id body");
        return userService.updateUser(request);
    }

//  для совместимости также
    @PutMapping(value = "/userDetailsUpdate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновить пользователя по id")
    public UserResponse updateByQuery(@Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(request);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя (и его роль если не используется)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public void deleteById(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

//  для совместимости
    @DeleteMapping(value = "/users", params = "userID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public void deleteByQuery(@RequestParam("userID") UUID id) {
        userService.deleteUser(id);
    }
}