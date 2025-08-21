package org.tasker.usersService.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Ответ с данными пользователя")
public record UserResponse(
        @Schema(example = "11111111-1111-1111-1111-111111111111")
        UUID id,

        @Schema(example = "Иванов Иван Иванович")
        String fio,

        @Schema(example = "+79990000001")
        String phoneNumber,

        @Schema(example = "https://example.com/avatar.png")
        String avatar,

        @Schema(example = "USER")
        String role
) {}