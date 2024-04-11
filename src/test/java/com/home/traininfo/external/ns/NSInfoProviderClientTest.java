package com.home.traininfo.external.ns;

import com.home.traininfo.domain.Status;
import com.home.traininfo.external.TrainInfo;
import com.home.traininfo.external.webtargetprovider.WebTargetProvider;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NSInfoProviderClientTest {

    @Mock
    private WebTargetProvider webTargetProvider;
    @Mock
    private WebTarget webTarget;
    @Mock
    private Invocation.Builder builder;
    @Mock
    private Invocation invocation;
    @Mock
    private Response response;

    private NSInfoProviderClient nsInfoProviderClient;

    @BeforeEach
    void setUp() {
        mockWebTrarget();
        nsInfoProviderClient = new NSInfoProviderClient("localhost", "/trains",
                        "key", webTargetProvider);
    }

    @Test
    @DisplayName("Test when there is a ProcessingException")
    void testGetDepartureTrains() {
        when(invocation.invoke()).thenThrow(new ProcessingException("error"));
        List<TrainInfo> departureTrains = nsInfoProviderClient.getDepartureTrains("123");
        assertTrue(departureTrains.isEmpty());
    }

    @Test
    @DisplayName("Test when there is an internal server error")
    void testGetDepartureTrains2() {
        when(response.getStatus()).thenReturn(500);
        List<TrainInfo> departureTrains = nsInfoProviderClient.getDepartureTrains("123");
        assertTrue(departureTrains.isEmpty());
    }

    @Test
    @DisplayName("Test with a successful response")
    void testGetDepartureTrains3() {
        when(response.getStatus()).thenReturn(200);
        when(response.readEntity(String.class)).thenReturn(getResponse());
        List<TrainInfo> departureTrains = nsInfoProviderClient.getDepartureTrains("123");
        assertAll(
                () -> assertFalse(departureTrains.isEmpty()),
                () -> assertEquals(1,departureTrains.size()),
                () -> assertEquals("Arnhem Centraal", departureTrains.get(0).direction()),
                () -> assertEquals("ST", departureTrains.get(0).trainCategory()),
                () -> assertEquals("08:17", departureTrains.get(0).actualDepartureTime()),
                () -> assertEquals("2", departureTrains.get(0).actualTrack()),
                () -> assertEquals(Status.CANCELLED, departureTrains.get(0).getStatus()));
    }

    private void mockWebTrarget() {
        when(webTargetProvider.getWebTarget(any())).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.buildGet()).thenReturn(invocation);
        when(invocation.invoke()).thenReturn(response);
    }

    private String getResponse() {
        return """
                {
                  "payload": {
                    "source": "PPV",
                    "departures": [
                      {
                        "direction": "Arnhem Centraal",
                        "name": "Arriva 31117",
                        "plannedDateTime": "2023-09-28T08:17:00+0200",
                        "plannedTimeZoneOffset": 120,
                        "actualDateTime": "2023-09-28T08:17:00+0200",
                        "actualTimeZoneOffset": 120,
                        "plannedTrack": "2",
                        "actualTrack": "2",
                        "product": {
                          "number": "31117",
                          "categoryCode": "ST",
                          "shortCategoryName": "Arriva Stoptrein",
                          "longCategoryName": "Stoptrein",
                          "operatorCode": "Arriva",
                          "operatorName": "Arriva",
                          "type": "TRAIN"
                        },
                        "trainCategory": "ST",
                        "cancelled": true,
                        "routeStations": [
                          {
                            "uicCode": "8400359",
                            "mediumName": "Kesteren"
                          },
                          {
                            "uicCode": "8400494",
                            "mediumName": "Opheusden"
                          },
                          {
                            "uicCode": "8400315",
                            "mediumName": "Hemmen-Dodew."
                          },
                          {
                            "uicCode": "8400207",
                            "mediumName": "Elst"
                          }
                        ],
                        "messages": [
                          {
                            "message": "Stopt niet in Arnhem Zuid",
                            "style": "INFO"
                          }
                        ],
                        "departureStatus": "ON_STATION"
                      }
                    ]
                  }
                }
                """;
    }
}