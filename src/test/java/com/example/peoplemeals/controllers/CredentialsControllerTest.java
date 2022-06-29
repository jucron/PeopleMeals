package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.services.CredentialsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testUtils.JsonConverter.asJsonString;

@ExtendWith(MockitoExtension.class)
class CredentialsControllerTest {

    private final String BASE_URL = "/credentials/";
    @InjectMocks
    private CredentialsController credentialsController;
    @Mock
    private CredentialsService credentialsService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(credentialsController).build();
    }

    @Nested
    class CorrectRequests {
        @Test
        void getUsers() throws Exception {
            //given
            int defaultPageNo = 0;
            int defaultPageSize = 10;
            String defaultSortBy = "username";
            //when
            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(credentialsService).getAll(defaultPageNo, defaultPageSize, defaultSortBy);
        }

        @Test
        void createUser() throws Exception {
            //given
            UserForm form = new UserForm();
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(form)))
                    .andExpect(status().isCreated());
            verify(credentialsService).createUser(form);
        }

        @Test
        void deactivateUser() throws Exception {
//            given
            String username = "username-example";
            //when
            mockMvc.perform(delete(BASE_URL + username)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(credentialsService).deactivateUser(username);
        }
    }

    @Nested
    class FailRequests {
        @Test
        void createUser_emptyBody() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
            verify(credentialsService, times(0)).createUser(any());
        }

        @Test
        void deactivateUser_emptyUsername() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed());
            verify(credentialsService, times(0)).deactivateUser(any());
        }
    }
}