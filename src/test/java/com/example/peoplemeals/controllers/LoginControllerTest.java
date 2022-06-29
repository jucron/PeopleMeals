package com.example.peoplemeals.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private final String BASE_URL = "/";
    @InjectMocks
    LoginController loginController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new CustomRestExceptionHandler())
                .build();
    }

    @Test
    void getLogin() throws Exception {
        //when and then
        mockMvc.perform(get(BASE_URL + "login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.message", equalTo(LoginController.loginMessage)));
    }

    @Test
    void successLogin() throws Exception {
        //when and then
        mockMvc.perform(get(BASE_URL + "success")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.message", equalTo(LoginController.successMessage)));
    }

    @Test
    void logout() throws Exception {
        //when and then
        mockMvc.perform(get(BASE_URL + "logout_ok")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.message", equalTo(LoginController.logoutMessage)));
    }

    @Test
    void failLogin() throws Exception {
        //when and then
        mockMvc.perform(post(BASE_URL + "failure")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }
}