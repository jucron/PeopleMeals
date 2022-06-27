package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.services.PlanningService;
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
class PlanningControllerTest {
    /** Expected functionalities:
    •	Associate, remove a person to a dish on a specific day (planning/meal)
    •	List people for a restaurant on a specific day (planning day)
    •	List people for a specific dish on a specific day (planning/meals)
    •	People who do not have dishes assigned on a specific day
    •   v2: get, getAll
     NOTE: Tested in deeper layers: Wrong Format requests; Element Not In DB
     */
     
    @InjectMocks
    private PlanningController planningController;
    @Mock
    private PlanningService planningService;

    private MockMvc mockMvc;
    private final String BASE_URL = PlanningController.BASE_URL;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(planningController).build();
    }

    @Nested
    class CorrectRequests {
        //given
        private final PlanningDTO PLANNING_DTO = PojoExampleCreation.createPlanningDTOExample(1);

        @Test
        void getAllPlanningsFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(planningService).getAll();
        }

        @Test
        void getASinglePlanningFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL + PLANNING_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(planningService).get(PLANNING_DTO.getUuid().toString());
        }

        @Test
        void associateAPersonToDishAndRestaurantAndDay() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(new AssociateForm())))
                    .andExpect(status().isOk());
            verify(planningService).associate(any(AssociateForm.class));
        }

        @Test
        void disassociateAPersonToDishAndRestaurantAndDay() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(new AssociateForm())))
                    .andExpect(status().isOk());
            verify(planningService).disassociate(any(AssociateForm.class));
        }

        @Test
        void removeAPlanningWithUuid() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL + PLANNING_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(planningService).remove(PLANNING_DTO.getUuid().toString());
        }

        @Nested
        class GetMethods {
            private final String EXAMPLE_UUID = "restaurant-uuid";
            private final String DAY_OF_WEEK = "AnyDayOfWeek";

            @Test
            void getPersonDTOListByRestaurantAndDayOfWeek() throws Exception {
                //when
                mockMvc.perform(get(BASE_URL + "restaurant/" + EXAMPLE_UUID + "/" + DAY_OF_WEEK)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                verify(planningService).getPersonListByRestaurantAndDay(EXAMPLE_UUID, DAY_OF_WEEK);
            }
            @Test
            void getPersonDTOListByDishAndDayOfWeek() throws Exception {
                //when
                mockMvc.perform(get(BASE_URL + "dish/" + EXAMPLE_UUID + "/" + DAY_OF_WEEK)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                verify(planningService).getPersonListByDishAndDay(EXAMPLE_UUID, DAY_OF_WEEK);
            }
            @Test
            void getPersonDTOListWithNoDishByDayOfWeek() throws Exception {
                //when
                mockMvc.perform(get(BASE_URL + "no_dish/" + DAY_OF_WEEK)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                verify(planningService).getPersonListWithNoDishByDay(DAY_OF_WEEK);
            }
        }
    }

    @Nested
    class FailRequests {
        @Test
        void associateAPersonWithEmptyBody() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
            verify(planningService, times(0)).associate(any());
        }

        @Test
        void disassociateAPersonWithEmptyBody() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
            verify(planningService, times(0)).disassociate(any());
        }

        @Nested
        class Fail_getMethods {
            private final String EXAMPLE_PARAMETER = "example-parameter";

            @Test
            void Fail_getPersonListWithNoDishByDay_missingParameters() throws Exception {
                //when
                mockMvc.perform(get(BASE_URL + "no_dish/")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()); //todo: why respond ok?
                verify(planningService, times(0))
                        .getPersonListWithNoDishByDay(any());
            }

            @Nested
            class Fail_getPersonDTOListByRestaurantAndDayOfWeek {
                @Test
                void missingOneParameter() throws Exception {
                    //when
                    mockMvc.perform(get(BASE_URL + "restaurant/" + EXAMPLE_PARAMETER)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());
                    verify(planningService, times(0))
                            .getPersonListByRestaurantAndDay(any(), any());
                }

                @Test
                void missingAllParameters() throws Exception {
                    //when
                    mockMvc.perform(get(BASE_URL + "restaurant/")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk()); //todo: why responded ok?
                    verify(planningService, times(0))
                            .getPersonListByRestaurantAndDay(any(), any());
                }
            }

            @Nested
            class Fail_getPersonListByDishAndDay {
                @Test
                void missingOneParameter() throws Exception {
                    //when
                    mockMvc.perform(get(BASE_URL + "dish/" + EXAMPLE_PARAMETER)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());
                    verify(planningService, times(0))
                            .getPersonListByDishAndDay(any(), any());
                }

                @Test
                void missingAllParameters() throws Exception {
                    //when
                    mockMvc.perform(get(BASE_URL + "dish/")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk()); //todo: why responded ok?
                    verify(planningService, times(0))
                            .getPersonListByDishAndDay(any(), any());
                }
            }
        }
    }
}