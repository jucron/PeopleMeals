package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
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
import testUtils.PojoExampleCreation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testUtils.JsonConverter.asJsonString;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {
    /** Expected endpoints:
        •	Add, remove, edit dishes (CRUD)
        •   v2: get, getAll
        NOTE: Tested in deeper layers: Wrong Format requests; Element Not In DB
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
        //given
        private final DishDTO DISH_DTO = PojoExampleCreation.createDishDTOExample(1);

        @Test
        void getAllDishesFromRepo() throws Exception {
            //given
            int defaultPageNo = 0;
            int defaultPageSize = 10;
            String defaultSortBy = "name";
            //when
            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(dishService).getAll(defaultPageNo, defaultPageSize, defaultSortBy);
        }

        @Test
        void getADishFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL + DISH_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(dishService).get(DISH_DTO.getUuid().toString());
        }

        @Test
        void addADishToRepo() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(DISH_DTO)))
                    .andExpect(status().isCreated());
            verify(dishService,times(1)).add(DISH_DTO);
        }

        @Test
        void removeADishFromRepo() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL + DISH_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(dishService).remove(DISH_DTO.getUuid().toString());
        }

        @Test
        void updateADishFromRepo() throws Exception {
            //given
            Dish dish = PojoExampleCreation.createDishExample(2);
            //when
            mockMvc.perform(put(BASE_URL + dish.getUuid())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(DISH_DTO)))
                    .andExpect(status().isOk());
            verify(dishService).update(dish.getUuid().toString(),DISH_DTO);
        }
    }

    @Nested
    class FailRequests {
        @Test
        void emptyBody_Add() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
            verify(dishService,times(0)).add(any());
        }

        @Test
        void emptyUuid_Delete() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed());
            verify(dishService,times(0)).remove(any());
        }

        @Nested
        class FailUpdate {
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