package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.services.DishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;

import static com.example.peoplemeals.helpers.JsonConverter.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomRestExceptionHandlerTest {
    /**
     * @ExceptionTestsTypes are being simulated with DISH Controller, for simplicity
     * Services methods are being stubbed to throw expected Exception
     */

    @Nested
    class DishControllerExceptions {

        @InjectMocks
        private DishController dishController;

        @Mock
        private DishServiceImpl dishService;

        private MockMvc mockMvc;

        private final String BASE_URL = "/dishes/";
        private final String exceptionCustomMessage = "Custom_message";
        private final String dishUuid = "dish-uuid";
        private final DishDTO dishDTOThatWillResultError = new DishDTO();

        @BeforeEach
        void setUp() {
            this.mockMvc = MockMvcBuilders.standaloneSetup(dishController)
                    .setControllerAdvice(new CustomRestExceptionHandler())
                    .build();
        }

        @Test
        void handleIllegalArgumentException() throws Exception {
            //given
            when(dishService.update(dishUuid,dishDTOThatWillResultError)).thenThrow(new IllegalArgumentException(
                    exceptionCustomMessage));
            //when and then
            mockMvc.perform(put(BASE_URL + dishUuid)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dishDTOThatWillResultError)))
//                .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(exceptionCustomMessage)));
            verify(dishService).update(dishUuid, dishDTOThatWillResultError);
        }

        @Test
        void handleNullPointerException() throws Exception {
            //given
            when(dishService.add(dishDTOThatWillResultError)).thenThrow(new NullPointerException());
            //when and then
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dishDTOThatWillResultError)))
//                .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(CustomRestExceptionHandler.GENERIC_EXCEPTION_MESSAGE)));
            verify(dishService).add(dishDTOThatWillResultError);

        }

        @Test
        void handleNoSuchElementException() throws Exception {
            //given
            doThrow(new NoSuchElementException(exceptionCustomMessage))
                    .when(dishService).remove(dishUuid);
            //when and then
            mockMvc.perform(delete(BASE_URL + dishUuid)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(exceptionCustomMessage)));
            verify(dishService).remove(dishUuid);
        }

        @Test
        void handleAnyOtherException() throws Exception {
            //given
            when(dishService.update(dishUuid, dishDTOThatWillResultError)).thenThrow(new RuntimeException(
                    CustomRestExceptionHandler.GENERIC_EXCEPTION_MESSAGE));
            //when and then
            mockMvc.perform(put(BASE_URL + dishUuid)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dishDTOThatWillResultError)))
//                .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(CustomRestExceptionHandler.GENERIC_EXCEPTION_MESSAGE)));
            verify(dishService).update(dishUuid, dishDTOThatWillResultError);
        }
    }
}