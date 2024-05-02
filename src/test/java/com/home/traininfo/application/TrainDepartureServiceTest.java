package com.home.traininfo.application;

import com.home.traininfo.domain.Status;
import com.home.traininfo.domain.TrainDeparture;
import com.home.traininfo.external.TrainInfo;
import com.home.traininfo.external.TrainInfoProviderClient;
import com.home.traininfo.external.webtargetprovider.JerseyWebTargetProvider;
import com.home.traininfo.external.webtargetprovider.WebTargetProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainDepartureServiceTest {

    @Mock
    TrainInfoProviderClient infoProvider;
    NightTimeChecker defaultNightTimeChecker;
    WebTargetProvider webTargetProvider;
    TrainDepartureService trainDepartureService;

    @BeforeEach
    void setUp() {
        webTargetProvider = new JerseyWebTargetProvider();
        defaultNightTimeChecker = new NightTimeChecker(true,
                () -> LocalTime.of(6, 0));

    }

    @Test
    @DisplayName("Test when night mode is enabled and time is between 01:01 AM and 05:01 AM")
    void testTrainDepartureService() {
        //current time is 2 AM
        var nightTimeChecker = new NightTimeChecker(true, () -> LocalTime.of(2, 0));
        trainDepartureService = new TrainService("8400597", infoProvider, nightTimeChecker);
        assertTrue(trainDepartureService.getDepartureInfo().isEmpty());
    }

    @Test
    @DisplayName("Test with TrainDeparture details")
    void testTrainDepartureService2() {
        trainDepartureService = new TrainService("8400597",
                infoProvider, defaultNightTimeChecker);
        when(infoProvider.getDepartureTrains("8400597"))
                .thenReturn(List.of(getTrainInfo()));
        List<TrainDeparture> departureInfo = trainDepartureService.getDepartureInfo();
        assertAll(
                () -> assertFalse(departureInfo.isEmpty()),
                () -> assertEquals(1,departureInfo.size()),
                () -> assertEquals("Utrecht Centraal", departureInfo.get(0).direction()),
                () -> assertEquals("SP", departureInfo.get(0).trainCategory()),
                () -> assertEquals("08:12", departureInfo.get(0).departureTime()),
                () -> assertEquals("3", departureInfo.get(0).actualTrack()),
                () -> assertEquals(Status.INCOMING, departureInfo.get(0).status()));
    }

    private static @NotNull TrainInfo getTrainInfo() {
        return new TrainInfo("Utrecht Centraal", "08:12","08:15",
                "3", "SP", "Geldermalsen",
                "INCOMING", false);
    }


}