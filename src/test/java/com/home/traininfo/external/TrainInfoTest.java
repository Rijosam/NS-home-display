package com.home.traininfo.external;

import com.home.traininfo.domain.Status;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainInfoTest {

    @Test
    @DisplayName("Train with isCancelled TRUE")
    void getStatus() {
        var trainInfo = getTrainInfo("INCOMING", true);
        assertEquals(Status.CANCELLED, trainInfo.getStatus());

    }

    @Test
    @DisplayName("Train with status INCOMING")
    void getStatus2() {
        var trainInfo = getTrainInfo("INCOMING", false);
        assertEquals(Status.INCOMING, trainInfo.getStatus());

    }

    @Test
    @DisplayName("Train with status ON_STATION")
    void getStatus3() {
        var trainInfo = getTrainInfo("ON_STATION", false);
        assertEquals(Status.ON_STATION, trainInfo.getStatus());

    }

    @NotNull
    private TrainInfo getTrainInfo(String departureStatus, boolean isCancelled) {
        return new TrainInfo("Utrecht", "16:20",
                "3",
                "SPR",
                "Tiel",
                departureStatus,
                isCancelled
        );
    }


}