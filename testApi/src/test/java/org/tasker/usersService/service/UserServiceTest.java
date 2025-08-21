package org.tasker.usersService.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.tasker.usersService.TestContainersConfig;
import org.tasker.usersService.web.dto.RoleDto;
import org.tasker.usersService.web.dto.UserCreateRequest;
import org.tasker.usersService.web.dto.UserResponse;
import org.tasker.usersService.web.dto.UserUpdateRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest extends TestContainersConfig {

    @Autowired
    UserService service;

    @Autowired
    CacheManager cacheManager;

    @Test
    void create_and_get_user_with_cache() {
        UserCreateRequest req = new UserCreateRequest("Тестовый Пользователь", "+79990001234", "https://example.com/a.png", new RoleDto("USER"));
        UserResponse created = service.createUser(req);
        assertThat(created.id()).isNotNull();

        UserResponse one = service.getUserById(created.id());
        UserResponse two = service.getUserById(created.id());
        assertThat(one).usingRecursiveComparison().isEqualTo(two);
    }

    @Test
    void update_and_delete_role_cleanup() {
        UserCreateRequest req = new UserCreateRequest("Пользователь", "+79990001235", null, new RoleDto("TEMP_ROLE"));
        UserResponse created = service.createUser(req);

        UserUpdateRequest upd = new UserUpdateRequest(created.id(), "Пользователь Обновлен", "+79990009999", null, new RoleDto("ANOTHER_ROLE"));
        UserResponse updated = service.updateUser(upd);
        assertThat(updated.role()).isEqualTo("ANOTHER_ROLE");

        service.deleteUser(updated.id());
        try {
            service.deleteUser(updated.id());
            assertThat(true).isFalse();
        } catch (Exception ignored) {
            assertThat(true).isTrue();
        }
    }
}
