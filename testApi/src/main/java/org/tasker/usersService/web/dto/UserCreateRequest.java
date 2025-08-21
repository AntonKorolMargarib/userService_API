package org.tasker.usersService.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Запрос на создание пользователя")
public record UserCreateRequest(
        @Schema(example = "Иванов Иван Иванович")
        @NotBlank @Size(max = 255) String fio,

        @Schema(example = "+79990000001")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный номер телефона")
        String phoneNumber,

        @Schema(example = "https://example.com/avatar.png")
        @URL(message = "Некорректный URL аватарки")
        String avatar,

        @Valid @NotNull
        RoleDto role
) {}
