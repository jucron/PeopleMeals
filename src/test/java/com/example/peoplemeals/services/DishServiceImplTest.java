package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.DishRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    private DishService dishService;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private DishMapper dishMapper;
    @Captor
    private ArgumentCaptor<Dish> dishArgumentCaptor;

    private String parameterForComparison;

    @BeforeEach
    public void setUpForAll(){
        //Instantiate service class
        dishService = new DishServiceImpl(dishRepository,dishMapper);

    }

    @Nested
    class SuccessfulServices {
        @Test
        void getAllElements() {
            when(dishRepository.findAll()).thenReturn(List.of(new Dish()));
            //when
            dishService.getAll();
            //then
            verify(dishRepository).findAll();
            verify(dishMapper,times(1)).dishToDishDTO(any(Dish.class));
        }
        @Nested
        class GetAndRemoveMethods {
            private final String DISH_UUID = "dishUuid_example";

            @BeforeEach
            public void setUpCommonDataAndStubs(){
                when(dishRepository.findRequiredByUuid(anyString())).thenReturn((new Dish()));
            }

            @Test
            void getASingleElement() {
                //when
                dishService.get(DISH_UUID);
                //then
                verify(dishMapper,times(1)).dishToDishDTO(any(Dish.class));
            }

            @Test
            void removeAnExistingObject() {
                //when
                dishService.remove(DISH_UUID);
                verify(dishRepository).delete(any(Dish.class));
            }
            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(dishRepository).findRequiredByUuid(DISH_UUID);
            }
        }

        @Nested
        class AddAndUpdateMethods {

            @BeforeEach
            public void setUpCommonDataAndStubs(){
                //given data
                DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);
                Dish dish = PojoExampleCreation.createDishExample(2);
                dish.setUuid(null);
                //given stubbing
                when(dishMapper.dishDTOToDish(any(DishDTO.class))).thenReturn(dish);
                when(dishMapper.dishToDishDTO(any(Dish.class))).thenReturn(dishDTO);
                when(dishRepository.save(any(Dish.class))).thenReturn(dish);
            }

            @Test
            void addANewObjectToDatabase() {
                //when
                DishDTO dishSavedDTO = dishService.add(new DishDTO());
                //then
                verify(dishRepository).save(dishArgumentCaptor.capture());  //Dish is persisted

                Dish dishCaptured = dishArgumentCaptor.getValue();    //Capture the object saved
                assertNull(dishCaptured.getId());         //Assert that an ID is null before persisted
                assertNotNull(dishCaptured.getUuid());    //Assert that a UUID is assigned before persisted
            }

            @Test
            void updateAnExistingObjectFromDatabase() {
                //given
                String dishUuid = "dishUuid_example";
                long dishIdFromDB = 15L;
                when(dishRepository.findRequiredByUuid(anyString())).thenReturn((
                        new Dish().withId(dishIdFromDB)));
                //when
                DishDTO dishSavedDTO = dishService.update(dishUuid,new DishDTO());
                //then
                verify(dishRepository).findRequiredByUuid(dishUuid);       //Dish is fetched by ID
                verify(dishRepository).save(dishArgumentCaptor.capture());  //Dish is updated

                Dish dishCaptured = dishArgumentCaptor.getValue();      //Capture the object saved
                assertEquals(dishIdFromDB,dishCaptured.getId());   //Assert that ID was the same as fetched before persisted
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(dishMapper).dishDTOToDish(any(DishDTO.class));   //Entity DTO is mapped to original
                verify(dishMapper).dishToDishDTO(any(Dish.class));      //Entity is mapped back to DTO and returned
            }
        }
    }

    @Nested
    class FailedServices {
        @Test
        void anyService_NullObjects() {
            //given expected behavior
            when(dishRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            //when-then
            assertThrows(NullPointerException.class,()-> dishService.add(null));
            assertThrows(IllegalArgumentException.class,()-> dishService.remove(null));
            assertThrows(IllegalArgumentException.class,()-> dishService.update(null, new DishDTO()));
            assertThrows(NullPointerException.class,()-> dishService.update("some-uuid", null));
            assertThrows(IllegalArgumentException.class,()-> dishService.get(null));

        }

        @Nested
        class AccessingNonExistingObjectsInDatabase {
            private final String nonExistingUuid = "uuid-example";

            @BeforeEach
            void stubbingExpectedBehaviours() {
                when(dishRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
            }

            @Test
            void getNonExistingObject() {
                //when
                assertThrows(NoSuchElementException.class,()-> dishService.get(nonExistingUuid));
            }

            @Test
            void removeNonExistingObject() {
                //when
                assertThrows(NoSuchElementException.class,()-> dishService.remove(nonExistingUuid));
            }

            @Test
            void updateNonExistingObject() {
                //when
                assertThrows(NoSuchElementException.class,()-> dishService.update(nonExistingUuid, new DishDTO()));
            }
        }


    }
}