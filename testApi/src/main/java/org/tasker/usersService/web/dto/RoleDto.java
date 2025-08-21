package org.tasker.usersService.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Роль пользователя")
public record RoleDto(
        @Schema(example = "USER") @NotBlank
        String roleName
) {}
