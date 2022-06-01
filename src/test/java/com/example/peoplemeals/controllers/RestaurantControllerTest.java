package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.services.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.peoplemeals.helpers.JsonConverter.asJsonString;
import static com.example.peoplemeals.helpers.JsonConverter.localTimeAsJsonArray;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {
    /* Expected endpoints:
        •	Add, remove, edit restaurants (CRUD)
     */
    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantService restaurantService;

    private MockMvc mockMvc;
    private final String BASE_URL = RestaurantController.BASE_URL;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    void addARestaurantToRepo() throws Exception {
        //given
        RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);

        given(restaurantService.add(restaurantDTO)).willReturn(restaurantDTO);
        //when
        mockMvc.perform(post(BASE_URL + "/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(restaurantDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(restaurantDTO.getName())))
                .andExpect(jsonPath("$.closingTime", equalTo(localTimeAsJsonArray(
                        restaurantDTO.getClosingTime()))));

        verify(restaurantService,times(1)).add(restaurantDTO);
    }
    @Test
    void removeARestaurantFromRepo() throws Exception {
        //given
        RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);
        //when
        mockMvc.perform(delete(BASE_URL + "/remove/"+restaurantDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(restaurantService).remove(restaurantDTO.getId());
    }
    @Test
    void updateARestaurantFromRepo() throws Exception {
        //given
        Restaurant restaurant = PojoExampleCreation.createRestaurantExample(1);
        RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);
        given(restaurantService.update(restaurant.getId(), restaurantDTO)).willReturn(restaurantDTO);

        //when
        mockMvc.perform(put(BASE_URL + "/update/"+restaurant.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(restaurantDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(restaurantDTO.getName())))
                .andExpect(jsonPath("$.closingTime", equalTo(localTimeAsJsonArray(
                        restaurantDTO.getClosingTime()))));
        verify(restaurantService).update(restaurant.getId(),restaurantDTO);
    }
}