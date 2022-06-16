package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        void removeAnExistingObject() {
            //given
            String dishUuid = "dishUuid_example";
            when(dishRepository.findRequiredByUuid(anyString())).thenReturn((new Dish()));
            //when
            dishService.remove(dishUuid);
            //then
            verify(dishRepository).findRequiredByUuid(dishUuid);
            verify(dishRepository).delete(any(Dish.class));
        }
        @Nested
        class UploadTestingData {

            @BeforeEach
            public void setUpDataAndStubs(){
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
                verify(dishMapper).dishDTOToDish(any(DishDTO.class));   //DishDTO is mapped to be saved
                verify(dishRepository).save(dishArgumentCaptor.capture());  //Dish is persisted
                verify(dishMapper).dishToDishDTO(any(Dish.class));      //Dish is mapped back to DTO and returned

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
                verify(dishMapper).dishDTOToDish(any(DishDTO.class));   //DishDTO is mapped to be updated
                verify(dishRepository).save(dishArgumentCaptor.capture());  //Dish is updated
                verify(dishMapper).dishToDishDTO(any(Dish.class));      //Dish is mapped back to DTO and returned

                Dish dishCaptured = dishArgumentCaptor.getValue();      //Capture the object saved
                assertEquals(dishIdFromDB,dishCaptured.getId());   //Assert that ID was the same as fetched before persisted
            }
        }
    }

    @Nested
    class FailedServices {
        @Test
        void add_remove_update_NullObject() {
            //given expected behavior
            when(dishRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            //when-then
            assertThrows(NullPointerException.class,()-> dishService.add(null));
            assertThrows(IllegalArgumentException.class,()-> dishService.remove(null));
            assertThrows(IllegalArgumentException.class,()-> dishService.update(null, new DishDTO()));
            assertThrows(NullPointerException.class,()-> dishService.update("some-uuid", null));
        }

        @Nested
        class AccessingNonExistingObjectsInDatabase {
            private final String nonExistingUuid = "uuid-example";

            @BeforeEach
            void stubbingExpectedBehaviours() {
                when(dishRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
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