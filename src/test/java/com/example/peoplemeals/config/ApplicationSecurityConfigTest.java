package com.example.peoplemeals.config;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.domain.security.Role;
import com.example.peoplemeals.repositories.CredentialsRepository;
import com.example.peoplemeals.repositories.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testUtils.JsonConverter.asJsonString;

@ActiveProfiles(profiles = "default")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationSecurityConfigTest {

    private final String UUID_example = UUID.randomUUID().toString();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Nested
    @WithMockUser(authorities = {"user"})
    class AccessingWithUserRole {
        @Nested
        class CredentialsController {
            private final String BASE_URL = "/credentials/";

            @Test
            void get_CredentialsController() throws Exception {
                mvc.perform(get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());
            }

            @Test
            void post_CredentialsController() throws Exception {
                mvc.perform(post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new UserForm())))
                        .andExpect(status().isForbidden());
            }

            @Test
            void delete_CredentialsController() throws Exception {
                String USERNAME_example = "username-example";
                mvc.perform(delete(BASE_URL + USERNAME_example)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());
            }
        }

        @Nested
        class DishController {
            private final String BASE_URL = "/dishes/";
            private final DishDTO dishDTO = new DishDTO().withName("name_example");

            @Test
            void get_DishController() throws Exception {
                mvc.perform(get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

            @Test
            void post_DishController() throws Exception {
                mvc.perform(post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(dishDTO)))
                        .andExpect(status().isCreated());
            }

            @Test
            void delete_DishController() throws Exception {
                mvc.perform(delete(BASE_URL + UUID_example)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());
            }

            @Test
            void put_DishController() throws Exception {
                Dish dishSaved = dishRepository.save(new Dish()
                        .withName("any-name").withUuid(UUID.randomUUID()));
                mvc.perform(put(BASE_URL + dishSaved.getUuid().toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(dishDTO)))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @WithMockUser(authorities = {"admin"})
    class AccessingWithAdminRole {
        @Nested
        class CredentialsController {
            private final String BASE_URL = "/credentials/";

            @Test
            void get_CredentialsController() throws Exception {
                mvc.perform(get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

            @Test
            void post_CredentialsController() throws Exception {
                mvc.perform(post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new UserForm()
                                        .withUsername("username")
                                        .withPassword("password")
                                        .withRole(Role.ADMIN.toString()))))
                        .andExpect(status().isCreated());
            }

            @Test
            void delete_CredentialsController() throws Exception {
                Credentials credentialsSaved = credentialsRepository.save(new Credentials()
                        .withUsername("another_username").withPassword("password").withRole(Role.ADMIN.role));
                mvc.perform(delete(BASE_URL + credentialsSaved.getUsername())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        class DishController {
            private final String BASE_URL = "/dishes/";
            private final DishDTO dishDTO = new DishDTO().withName("name_example");

            @Test
            void get_DishController() throws Exception {
                mvc.perform(get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

            @Test
            void post_DishController() throws Exception {
                mvc.perform(post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(dishDTO)))
                        .andExpect(status().isCreated());
            }

            @Test
            void delete_DishController() throws Exception {
                Dish dishSaved = dishRepository.save(new Dish()
                        .withName("any-name").withUuid(UUID.randomUUID()));
                mvc.perform(delete(BASE_URL + dishSaved.getUuid().toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

            @Test
            void put_DishController() throws Exception {
                Dish dishSaved = dishRepository.save(new Dish()
                        .withName("any-name").withUuid(UUID.randomUUID()));
                mvc.perform(put(BASE_URL + dishSaved.getUuid().toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(dishDTO)))
                        .andExpect(status().isOk());
            }
        }
    }


}