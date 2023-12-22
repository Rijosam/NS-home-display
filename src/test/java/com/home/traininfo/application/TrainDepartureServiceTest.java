package com.home.traininfo.application;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.home.traininfo.domain.Status;
import com.home.traininfo.external.TrainInfoProviderClient;
import com.home.traininfo.external.ns.TrainInfoProvider;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainDepartureServiceTest {

    TrainDepartureService trainDepartureService;
    TrainInfoProviderClient infoProvider;

    String baseUrl = "http://localhost:8089";
    String uriPath = "/api/v2/departures";

    WireMockServer wireMockServer = new WireMockServer(options()
            .port(8089));

    @BeforeEach
    void setUp() {
        infoProvider = new TrainInfoProvider(baseUrl, uriPath, "key");
        wireMockServer.start();
        setStubs();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("Success response for Tiel station code")
    void testTrainDepartureService() {
        trainDepartureService = new TrainService("8400596",infoProvider);
        var departureInfo = trainDepartureService.getDepartureInfo();
        assertAll(
                () -> assertEquals(6, departureInfo.size()),
                () -> assertEquals("Arnhem Centraal", departureInfo.get(0).direction()),
                () -> assertEquals(Status.ON_STATION, departureInfo.get(0).status()),
                () -> assertEquals("08:17", departureInfo.get(0).actualDepartureTime()),
                () -> assertEquals("2", departureInfo.get(0).actualTrack()),
                () -> assertEquals("Kesteren, Opheusden, Hemmen-Dodew., Elst",
                        departureInfo.get(0).routeStations())
        );
    }

    @Test
    @DisplayName("Bad request response departure api")
    void testTrainDepartureService2() {
        trainDepartureService = new TrainService("840000",infoProvider);
        var departureInfo = trainDepartureService.getDepartureInfo();
        assertAll(
                () -> assertTrue(departureInfo.isEmpty())
        );
    }
    @Test
    @DisplayName("Not found response departure api")
    void testTrainDepartureService3() {
        trainDepartureService = new TrainService("840001",infoProvider);
        var departureInfo = trainDepartureService.getDepartureInfo();
        assertAll(
                () -> assertTrue(departureInfo.isEmpty())
        );
    }

    @Test
    @DisplayName("Response with a cancelled train")
    void testTrainDepartureService4() {
        trainDepartureService = new TrainService("8400597",infoProvider);
        var departureInfo = trainDepartureService.getDepartureInfo();
        assertAll(
                () -> assertFalse(departureInfo.isEmpty()),
                () -> assertEquals(Status.CANCELLED,departureInfo.get(0).status())
        );
    }


    void setStubs() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v2/departures?uicCode=8400597&maxJourneys=6"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("response-8400597.json")));

        wireMockServer.stubFor(get(urlEqualTo("/api/v2/departures?uicCode=8400596&maxJourneys=6"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("response-8400596.json")));

        wireMockServer.stubFor(get(urlEqualTo("/api/v2/departures?uicCode=840000&maxJourneys=6"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.SC_BAD_REQUEST).withBodyFile("response-8400000.json")));
      wireMockServer.stubFor(get(urlEqualTo("/api/v2/departures?uicCode=840001&maxJourneys=6"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.SC_NOT_FOUND).withBodyFile("response-8400001.json")));
    }

}