package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
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
class DishServiceImplTest {

    private DishService dishService;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private DishMapper dishMapper;
    @Captor
    private ArgumentCaptor<Dish> dishArgumentCaptor;

    @BeforeEach
    public void setUpForAll(){
        //Instantiate service class
        dishService = new DishServiceImpl(dishRepository,dishMapper);
    }

    @Nested
    class SuccessfulServices {
        private final String DISH_UUID = "Uuid_example";

        @Test
        void removeAnExistingObject() {
            //given
            long idExample = 15L;
            when(dishRepository.findIdRequiredByUuid(anyString())).thenReturn(idExample);
            //when
            dishService.remove(DISH_UUID);
            //then
            verify(dishRepository).deleteById(idExample);
            verify(dishRepository).findIdRequiredByUuid(DISH_UUID);
        }

        @Nested
        class GetAndGetAllMethods {
            @Test
            void getElements() {
                //given
                int defaultPageNo = 0;
                int defaultPageSize = 10;
                String defaultSortBy = "name";
                when(dishRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(new Dish())));
                //when
                dishService.getAll(defaultPageNo, defaultPageSize, defaultSortBy);
                //then
                verify(dishRepository).findAll(any(PageRequest.class));
            }

            @Test
            void getASingleElement() {
                //given
                when(dishRepository.findRequiredByUuid(anyString())).thenReturn((new Dish()));
                //when
                dishService.get(DISH_UUID);
                //then
                verify(dishRepository).findRequiredByUuid(DISH_UUID);
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(dishMapper,times(1)).dishToDishDTO(any(Dish.class));
            }
        }

        @Nested
        class AddAndUpdateMethods {

            @BeforeEach
            public void setUpCommonDataAndStubs(){
                //given data
                DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);
                Dish dish = PojoExampleCreation.createDishExample(2);
                dish.setUuid(null); //setting UUID null, to check if 'add' method assign a value
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
                verify(dishRepository).save(dishArgumentCaptor.capture());  //Entity is persisted

                Dish dishCaptured = dishArgumentCaptor.getValue();    //Capture the object saved
                assertNull(dishCaptured.getId());         //Assert that an ID is null before persisted
                assertNotNull(dishCaptured.getUuid());    //Assert that a UUID is assigned before persisted
            }

            @Test
            void updateAnExistingObjectFromDatabase() {
                //given
                String dishUuid = UUID.randomUUID().toString();
                long dishIdFromDB = 15L;
                when(dishRepository.findIdRequiredByUuid(dishUuid)).thenReturn((
                        dishIdFromDB));
                //when
                DishDTO dishUpdatedDTO = dishService.update(dishUuid,new DishDTO());
                //then
                verify(dishRepository).findIdRequiredByUuid(dishUuid);       //Entity ID is fetched by UUID
                verify(dishRepository).save(dishArgumentCaptor.capture());  //Entity is updated

                Dish dishCaptured = dishArgumentCaptor.getValue();      //Capture the object saved
                assertEquals(dishIdFromDB,dishCaptured.getId());   //Assert that ID was the same as fetched before persisted
                assertEquals(dishUuid,dishCaptured.getUuid().toString());   //Assert that UUID was the same as fetched before persisted
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
        void anyServiceWithNullObjects() {
            //given expected behavior
            when(dishRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            when(dishRepository.findIdRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
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

            @Test
            void getNonExistingObject() {
                //given expected behavior
                when(dishRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                //when
                assertThrows(NoSuchElementException.class,()-> dishService.get(nonExistingUuid));
            }

            @Nested
            class removeAndUpdateMethods {
                @BeforeEach
                void stubbingExpectedBehaviours() {
                    when(dishRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
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
}