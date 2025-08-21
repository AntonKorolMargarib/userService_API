package org.tasker.usersService.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Schema(description = "Запрос на обновление пользователя")
public record UserUpdateRequest(
        @Schema(example = "11111111-1111-1111-1111-111111111111")
        @NotNull UUID id,

        @Schema(example = "Петров Петр Петрович")
        @NotBlank @Size(max = 255) String fio,

        @Schema(example = "+79990000002")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$") String phoneNumber,

        @Schema(example = "https://example.com/avatar2.png")
        @URL String avatar,

        @Valid @NotNull RoleDto role
) {}