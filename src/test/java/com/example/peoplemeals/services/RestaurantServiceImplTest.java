package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.RestaurantMapper;
import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.repositories.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import testUtils.PojoExampleCreation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper restaurantMapper;
    @Captor
    private ArgumentCaptor<Restaurant> restaurantArgumentCaptor;

    @BeforeEach
    public void setUpForAll(){
        //Instantiate service class
        restaurantService = new RestaurantServiceImpl(restaurantRepository,restaurantMapper);
    }

    @Nested
    class SuccessfulServices {
        private final String RESTAURANT_UUID = "Uuid_example";

        @Test
        void removeAnExistingObject() {
            //given
            long idExample = 15L;
            when(restaurantRepository.findIdRequiredByUuid(anyString())).thenReturn(idExample);
            //when
            restaurantService.remove(RESTAURANT_UUID);
            //then
            verify(restaurantRepository).deleteById(idExample);
            verify(restaurantRepository).findIdRequiredByUuid(RESTAURANT_UUID);
        }

        @Nested
        class GetAndGetAllMethods {
            @Test
            void getElements() {
                when(restaurantRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(new Restaurant())));
                //when
                restaurantService.getAll(0, 10, "name");
                //then
                verify(restaurantRepository).findAll(any(PageRequest.class));
            }

            @Test
            void getASingleElement() {
                //given
                when(restaurantRepository.findRequiredByUuid(anyString())).thenReturn((new Restaurant()));
                //when
                restaurantService.get(RESTAURANT_UUID);
                //then
                verify(restaurantRepository).findRequiredByUuid(RESTAURANT_UUID);
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(restaurantMapper,times(1)).restaurantToRestaurantDTO(any(Restaurant.class));
            }
        }

        @Nested
        class AddAndUpdateMethods {

            @BeforeEach
            public void setUpCommonDataAndStubs(){
                //given data
                RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);
                Restaurant restaurant = PojoExampleCreation.createRestaurantExample(2);
                restaurant.setUuid(null); //setting UUID null, to check if 'add' method assign a value
                //given stubbing
                when(restaurantMapper.restaurantDTOToRestaurant(any(RestaurantDTO.class))).thenReturn(restaurant);
                when(restaurantMapper.restaurantToRestaurantDTO(any(Restaurant.class))).thenReturn(restaurantDTO);
                when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
            }

            @Test
            void addANewObjectToDatabase() {
                //when
                RestaurantDTO restaurantSavedDTO = restaurantService.add(new RestaurantDTO());
                //then
                verify(restaurantRepository).save(restaurantArgumentCaptor.capture());  //Entity is persisted

                Restaurant restaurantCaptured = restaurantArgumentCaptor.getValue();    //Capture the object saved
                assertNull(restaurantCaptured.getId());         //Assert that an ID is null before persisted
                assertNotNull(restaurantCaptured.getUuid());    //Assert that a UUID is assigned before persisted
            }

            @Test
            void updateAnExistingObjectFromDatabase() {
                //given
                String restaurantUuid = UUID.randomUUID().toString();
                long restaurantIdFromDB = 15L;
                when(restaurantRepository.findIdRequiredByUuid(restaurantUuid)).thenReturn((
                        restaurantIdFromDB));
                //when
                RestaurantDTO restaurantUpdatedDTO = restaurantService.update(restaurantUuid,new RestaurantDTO());
                //then
                verify(restaurantRepository).findIdRequiredByUuid(restaurantUuid);       //Entity ID is fetched by UUID
                verify(restaurantRepository).save(restaurantArgumentCaptor.capture());  //Entity is updated

                Restaurant restaurantCaptured = restaurantArgumentCaptor.getValue();      //Capture the object saved
                assertEquals(restaurantIdFromDB,restaurantCaptured.getId());   //Assert that ID was the same as fetched before persisted
                assertEquals(restaurantUuid,restaurantCaptured.getUuid().toString());   //Assert that UUID was the same as fetched before persisted
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(restaurantMapper).restaurantDTOToRestaurant(any(RestaurantDTO.class));   //Entity DTO is mapped to original
                verify(restaurantMapper).restaurantToRestaurantDTO(any(Restaurant.class));      //Entity is mapped back to DTO and returned
            }
        }
    }

    @Nested
    class FailedServices {
        @Test
        void anyServiceWithNullObjects() {
            //given expected behavior
            when(restaurantRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            when(restaurantRepository.findIdRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            //when-then
            assertThrows(NullPointerException.class,()-> restaurantService.add(null));
            assertThrows(IllegalArgumentException.class,()-> restaurantService.remove(null));
            assertThrows(IllegalArgumentException.class,()-> restaurantService.update(null, new RestaurantDTO()));
            assertThrows(NullPointerException.class,()-> restaurantService.update("some-uuid", null));
            assertThrows(IllegalArgumentException.class,()-> restaurantService.get(null));
        }

        @Nested
        class AccessingNonExistingObjectsInDatabase {
            private final String nonExistingUuid = "uuid-example";

            @Test
            void getNonExistingObject() {
                //given expected behavior
                when(restaurantRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                //when
                assertThrows(NoSuchElementException.class,()-> restaurantService.get(nonExistingUuid));
            }

            @Nested
            class removeAndUpdateMethods {
                @BeforeEach
                void stubbingExpectedBehaviours() {
                    when(restaurantRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                }

                @Test
                void removeNonExistingObject() {
                    //when
                    assertThrows(NoSuchElementException.class,()-> restaurantService.remove(nonExistingUuid));
                }

                @Test
                void updateNonExistingObject() {
                    //when
                    assertThrows(NoSuchElementException.class,()-> restaurantService.update(nonExistingUuid, new RestaurantDTO()));
                }
            }
        }
    }

}

 