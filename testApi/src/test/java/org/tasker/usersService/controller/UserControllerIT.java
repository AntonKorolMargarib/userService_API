package org.tasker.usersService.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.tasker.usersService.TestContainersConfig;
import org.tasker.usersService.web.dto.RoleDto;
import org.tasker.usersService.web.dto.UserCreateRequest;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT extends TestContainersConfig {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @Test
    void full_flow_create_get_update_delete() throws Exception {
        var create = new UserCreateRequest("Интеграционный Тест", "+79990001236", "https://example.com/b.png", new RoleDto("USER"));

        String created = mvc.perform(post("/api/createNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.fio", is("Интеграционный Тест")))
                .andReturn().getResponse().getContentAsString();

        UUID id = UUID.fromString(om.readTree(created).get("id").asText());

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())));

        String updateJson = "{" +
                "\"id\":\"" + id + "\"," +
                "\"fio\":\"Интеграционный Тест 2\"," +
                "\"phoneNumber\":\"+79990009998\"," +
                "\"avatar\":\"https://example.com/c.png\"," +
                "\"role\":{\"roleName\":\"MANAGER\"}}";

        mvc.perform(put("/api/userDetailsUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio", is("Интеграционный Тест 2")))
                .andExpect(jsonPath("$.role", is("MANAGER")));

        mvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }
}
