package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.services.RestaurantService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {
    private final String BASE_URL = "/restaurants/";

    @Mock
    private RestaurantService restaurantService;

    private MockMvc mockMvc;
    /** Expected endpoints:
     •	Add, remove, edit Restaurants (CRUD)
     •   v2: get, getAll
     NOTE: Tested in deeper layers: Wrong Format requests; Element Not In DB
     */

    @InjectMocks
    private RestaurantController restaurantController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Nested
    class CorrectRequests {
        //given
        private final RestaurantDTO PERSON_DTO = PojoExampleCreation.createRestaurantDTOExample(1);

        @Test
        void getAllRestaurantsFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(restaurantService).getAll();
        }

        @Test
        void getARestaurantFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL + PERSON_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(restaurantService).get(PERSON_DTO.getUuid().toString());

        }

        @Test
        void addARestaurantToRepo() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(PERSON_DTO)))
                    .andExpect(status().isCreated());
            verify(restaurantService,times(1)).add(PERSON_DTO);
        }

        @Test
        void removeARestaurantFromRepo() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL + PERSON_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(restaurantService).remove(PERSON_DTO.getUuid().toString());
        }

        @Test
        void updateARestaurantFromRepo() throws Exception {
            //given
            Restaurant Restaurant = PojoExampleCreation.createRestaurantExample(2);
            //when
            mockMvc.perform(put(BASE_URL + Restaurant.getUuid())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(PERSON_DTO)))
                    .andExpect(status().isOk());
            verify(restaurantService).update(Restaurant.getUuid().toString(), PERSON_DTO);
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
            verify(restaurantService,times(0)).add(any());
        }

        @Test
        void emptyUuid_Delete() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed());
            verify(restaurantService,times(0)).remove(any());
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
                verify(restaurantService,times(0)).update(any(), any());
            }

            @Test
            void emptyUuid() throws Exception {
                //when
                mockMvc.perform(put(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new RestaurantDTO())))
                        .andExpect(status().isMethodNotAllowed());
                verify(restaurantService,times(0)).update(any(), any());
            }

            @Test
            void emptyBody() throws Exception {
                //when
                mockMvc.perform(put(BASE_URL+"Some-uuid")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
                verify(restaurantService,times(0)).update(any(), any());
            }
        }
    }

     
}