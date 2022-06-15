package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.services.DishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
    /**@ExceptionTestsTypes are being simulated with DishController
     * Services methods are being stubbed to throw expected Exception*/

    @InjectMocks
    private DishController dishController;

    @Mock
    private DishServiceImpl dishService;

    private MockMvc mockMvc;

    private final String BASE_URL ="/dishes/";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(dishController)
                .setControllerAdvice(new CustomRestExceptionHandler())
                .build();
    }
    @Test
    void handleIllegalArgumentException() throws Exception {
        //given
        String dishUuid = "dish-uuid";
        DishDTO dishDTOThatWillResultError = new DishDTO();
        String exceptionCustomMessage = "Custom_message";
        when(dishService.update(dishUuid,dishDTOThatWillResultError)).thenThrow(new IllegalArgumentException(
                exceptionCustomMessage));
        //when and then
        mockMvc.perform(put(BASE_URL+dishUuid)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dishDTOThatWillResultError)))
//                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(exceptionCustomMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause", equalTo(IllegalArgumentException.class+"/null")));
        verify(dishService).update(dishUuid,dishDTOThatWillResultError);
    }
    @Test
    void handleNullPointerException() throws Exception {
        //given
        DishDTO dishDTOEmpty = new DishDTO();
        when(dishService.add(dishDTOEmpty)).thenThrow(new NullPointerException(
                CustomRestExceptionHandler.NULL_POINTER_EXCEPTION_MESSAGE));
        //when and then
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dishDTOEmpty)))
//                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(CustomRestExceptionHandler.NULL_POINTER_EXCEPTION_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause", equalTo(NullPointerException.class+"/null")));
        verify(dishService).add(dishDTOEmpty);

    }

    @Test
    void handleNoSuchElementException() throws Exception {
        //given
        String dishIdThatDoesNotExist = "dish-uuid";
        String exceptionCustomMessage = "Custom_message";
        doThrow(new NoSuchElementException(exceptionCustomMessage))
                .when(dishService).remove(dishIdThatDoesNotExist);
        //when and then
        mockMvc.perform(delete(BASE_URL+dishIdThatDoesNotExist)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(exceptionCustomMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause", equalTo(NoSuchElementException.class+"/null")));
        verify(dishService).remove(dishIdThatDoesNotExist);
    }

    @Test
    void handleAnyException() throws Exception {
        //given
        String dishId = "dish-uuid";
        DishDTO dishDTOThatWillResultError = new DishDTO();
        when(dishService.update(dishId,dishDTOThatWillResultError)).thenThrow(new RuntimeException(
                CustomRestExceptionHandler.GENERIC_EXCEPTION_MESSAGE));
        //when and then
        mockMvc.perform(put(BASE_URL+dishId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dishDTOThatWillResultError)))
//                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(CustomRestExceptionHandler.GENERIC_EXCEPTION_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause", equalTo(RuntimeException.class+"/null")));
        verify(dishService).update(dishId,dishDTOThatWillResultError);
    }
}