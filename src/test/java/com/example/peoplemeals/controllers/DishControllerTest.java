package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.services.DishService;
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

import static com.example.peoplemeals.helpers.JsonConverter.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {
    /* Expected endpoints:
        •	Add, remove, edit dishes (CRUD)
     */
    @InjectMocks
    private DishController dishController;

    @Mock
    private DishService dishService;

    private MockMvc mockMvc;
    private final String BASE_URL = "/dishes/";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();
    }

    @Nested
    class CorrectRequests {
        @Test
        void addADishToRepo() throws Exception {
            //given
            DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);

            given(dishService.add(dishDTO)).willReturn(dishDTO);
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dishDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", equalTo(dishDTO.getName())))
                    .andExpect(jsonPath("$.recipeUrl", equalTo(dishDTO.getRecipeUrl())));

            verify(dishService,times(1)).add(dishDTO);
        }

        @Test
        void removeADishFromRepo() throws Exception {
            //given
            DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);
            //when
            mockMvc.perform(delete(BASE_URL + dishDTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(dishService).remove(dishDTO.getUuid().toString());
        }

        @Test
        void updateADishFromRepo() throws Exception {
            //given
            Dish dish = PojoExampleCreation.createDishExample(1);
            DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(2);
            given(dishService.update(dish.getUuid().toString(), dishDTO)).willReturn(dishDTO);

            //when
            mockMvc.perform(put(BASE_URL + dish.getUuid())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dishDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", equalTo(dishDTO.getName())))
                    .andExpect(jsonPath("$.recipeUrl", equalTo(dishDTO.getRecipeUrl())));
            verify(dishService).update(dish.getUuid().toString(),dishDTO);
        }
    }

    @Nested
    class FailRequests {
        @Test
        void emptyBodyAddDish() throws Exception {

            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
            verify(dishService,times(0)).add(any());
        }
        @Test
        void emptyUuidDeleteDish() throws Exception {

            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed());
            verify(dishService,times(0)).remove(any());
        }
        @Nested
        class FailUpdateDish {
            @Test
            void emptyBodyAndUuid() throws Exception {

                //when
                mockMvc.perform(put(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isMethodNotAllowed());
                verify(dishService,times(0)).update(any(), any());
            }
            @Test
            void emptyUuid() throws Exception {

                //when
                mockMvc.perform(put(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new DishDTO())))
                        .andExpect(status().isMethodNotAllowed());
                verify(dishService,times(0)).update(any(), any());
            }
            @Test
            void emptyBody() throws Exception {

                //when
                mockMvc.perform(put(BASE_URL+"Some-uuid")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
                verify(dishService,times(0)).update(any(), any());
            }
        }

    }


}