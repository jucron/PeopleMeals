package com.example.peoplemeals.helpers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class DayOfWeekHelperTest {

    @Nested
    class SuccessTests {
        @Test
        void validateUtilityClassInstantiation() {
            final DayOfWeekHelper dayOfWeekHelperInstantiation = new DayOfWeekHelper();
            assertNotNull(dayOfWeekHelperInstantiation);
        }

        @Test
        void validateADayOfWeek() {
            //given
            DayOfWeek dayOfWeek1 = DayOfWeek.MONDAY;
            DayOfWeek dayOfWeek2 = DayOfWeek.WEDNESDAY;
            DayOfWeek dayOfWeek3 = DayOfWeek.SUNDAY;
            //when
            DayOfWeek dayOfWeekExtracted1 = DayOfWeekHelper.validateDayOfWeek(dayOfWeek1.toString());
            DayOfWeek dayOfWeekExtracted2 = DayOfWeekHelper.validateDayOfWeek(dayOfWeek2.toString());
            DayOfWeek dayOfWeekExtracted3 = DayOfWeekHelper.validateDayOfWeek(dayOfWeek3.toString());
            //then
            assertAll(
                    () -> assertEquals(dayOfWeek1, dayOfWeekExtracted1),
                    () -> assertEquals(dayOfWeek2, dayOfWeekExtracted2),
                    () -> assertEquals(dayOfWeek3, dayOfWeekExtracted3)
            );

        }
    }

    @Nested
    class FailTests {
        @Test
        void nullParameter() {
            assertThrows(IllegalArgumentException.class, () -> DayOfWeekHelper.validateDayOfWeek(null));
        }

        @Test
        void wrongFormat() {
            assertThrows(IllegalArgumentException.class, () -> DayOfWeekHelper.validateDayOfWeek("wrong-date-format"));
        }
    }

}