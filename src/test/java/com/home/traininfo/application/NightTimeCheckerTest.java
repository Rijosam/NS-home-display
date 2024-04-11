package com.home.traininfo.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NightTimeCheckerTest {

    private NightTimeChecker nightTimeChecker;

    @ParameterizedTest
    @DisplayName("Check whether time is before 01:01 AM and after 05:01 AM ")
    @ValueSource(ints =  {0,1,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,23})
    void testCheckNightTime(int hour) {
        nightTimeChecker = new NightTimeChecker(true,
                () -> LocalTime.of(hour, 0));
        assertFalse(nightTimeChecker.isNightTime());
    }

    @ParameterizedTest
    @DisplayName("Check whether time is between 01:01 AM and 05:01 AM")
    @ValueSource(ints =  {2,3,4})
    void testCheckNightTime2(int hour) {
        nightTimeChecker = new NightTimeChecker(true,
                () -> LocalTime.of(hour, 0));
        assertTrue(nightTimeChecker.isNightTime());
    }

    @Test
    @DisplayName("Check with nightMode off")
    void testCheckNightModeOff() {
        nightTimeChecker = new NightTimeChecker(false,
                () -> LocalTime.of(1, 15));
        assertFalse(nightTimeChecker.isNightTime());
    }


}