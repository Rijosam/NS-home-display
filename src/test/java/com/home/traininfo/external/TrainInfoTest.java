package com.home.traininfo.external;

import com.home.traininfo.domain.Status;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TrainInfoTest {

    @Test
    @DisplayName("Train with isCancelled TRUE")
    void testGetStatus() {
        var trainInfo = getTrainInfo("INCOMING", true);
        assertEquals(Status.CANCELLED, trainInfo.getStatus());

    }

    @Test
    @DisplayName("Train with status INCOMING")
    void testGetStatus2() {
        var trainInfo = getTrainInfo("INCOMING", false);
        assertEquals(Status.INCOMING, trainInfo.getStatus());

    }

    @Test
    @DisplayName("Train with status ON_STATION")
    void testGetStatus3() {
        var trainInfo = getTrainInfo("ON_STATION", false);
        assertEquals(Status.ON_STATION, trainInfo.getStatus());
    }

    @Test
    @DisplayName("Train with TimeToDeparture 14 min")
    void testGetFormattedTimeToDeparture() {
        var trainInfo = getTrainInfo(LocalTime.now().plusMinutes(15).toString());
        assertEquals("14 min", trainInfo.getFormattedTimeToDeparture());
    }

    @Test
    @DisplayName("Train with TimeToDeparture 1 hr 14 min")
    void testGetFormattedTimeToDeparture2() {
        var trainInfo = getTrainInfo(LocalTime.now().plusMinutes(75).toString());
        assertEquals("1 hr 14 min", trainInfo.getFormattedTimeToDeparture());
    }

    @Test
    @DisplayName("Train with TimeToDeparture 1 hr")
    void testGetFormattedTimeToDeparture3() {
        var trainInfo = getTrainInfo(LocalTime.now().plusMinutes(61).toString());
        assertEquals("1 hr", trainInfo.getFormattedTimeToDeparture());
    }

    @Test
    @DisplayName("Train with TimeToDeparture less than 1 min")
    void testGetFormattedTimeToDeparture4() {
        var trainInfo = getTrainInfo(LocalTime.now().plusMinutes(1).toString());
        assertEquals("<1 min", trainInfo.getFormattedTimeToDeparture());
    }


    @Test
    @DisplayName("Test with same plannedDepartureTime and actualDepartureTime")
    void testGetDepartureDelayInMinutes() {
        var trainInfo = getTrainInfo("16:20", "16:20");
        assertTrue(StringUtils.isBlank(trainInfo.getDepartureDelayInMinutes()));
    }

    @Test
    @DisplayName("Test with different plannedDepartureTime and actualDepartureTime")
    void testGetDepartureDelayInMinutes2() {
        var trainInfo = getTrainInfo("16:20", "16:27");
        assertAll(
                ()->assertFalse(StringUtils.isBlank(trainInfo.getDepartureDelayInMinutes())),
                ()->assertEquals("7",trainInfo.getDepartureDelayInMinutes())
        );
    }

    @NotNull
    private TrainInfo getTrainInfo(String departureStatus, boolean isCancelled) {
        return new TrainInfo("Utrecht", "16:20","16:20",
                "3",
                "SPR",
                "Tiel",
                departureStatus,
                isCancelled
        );
    }

   @NotNull
   private TrainInfo getTrainInfo(String actualDepartureTime) {
        return getTrainInfo( actualDepartureTime,actualDepartureTime);
    }

    @NotNull
    private TrainInfo getTrainInfo(String plannedDepartureTime, String actualDepartureTime) {
        return new TrainInfo("Utrecht", plannedDepartureTime, actualDepartureTime,
                "3",
                "SPR",
                "Tiel",
                "ON_STATION",
                false
        );
    }
}