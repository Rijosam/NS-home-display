package com.home.traininfo.external.ns;

import com.home.traininfo.external.TrainInfo;
import com.home.traininfo.external.TrainInfoProviderClient;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TrainInfoProvider implements TrainInfoProviderClient {

    private static final Jsonb jsonb = JsonbBuilder.create();

    private final String baseUrl;
    private final String uriPath;
    private final String subscriptionKey;

    private static final Logger log = LoggerFactory
            .getLogger(TrainInfoProvider.class);

    public TrainInfoProvider(@Value("${baseUrl}")String baseUrl,
                             @Value("${uriPath}")String uriPath,
                             @Value("${subscriptionKey}") String subscriptionKey) {
        this.baseUrl = baseUrl;
        this.uriPath = uriPath;
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    public List<TrainInfo> getDepartureTrains(String stationUicCode) {

        try (Response response = getTrainsInfo(stationUicCode)) {
            if (response.getStatus() == 200) {
                return getDeparturesPayload(response)
                        .payload()
                        .departures()
                        .stream()
                        .map(this::getTrainsInfo)
                        .toList();
            }
            else {
                log.error("Reisinformatie-api response with status {} and message {}",
                        response.getStatus(), response.readEntity(String.class));
                return Collections.emptyList();
            }
        } catch (ProcessingException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    private Response getTrainsInfo(final String stationUicCode) {
        log.info("Calling NS API");
        return getWebTarget(getUri(stationUicCode)).request()
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .buildGet()
                .invoke();
    }

    private DeparturesPayload getDeparturesPayload(Response response) {
        var departuresPayload = jsonb.fromJson
                (response.readEntity(String.class), DeparturesPayload.class);
        log.info("Response received");
        log.debug("Response {}", departuresPayload);
        return departuresPayload;
    }

    private WebTarget getWebTarget(URI uri) {
        return ClientBuilder.newBuilder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .build()
                .target(uri);
    }

    private URI getUri(String stationUicCode) {
        return UriBuilder
                .fromUri(baseUrl).path(uriPath)
                .queryParam("uicCode", stationUicCode)
                .queryParam("maxJourneys", 6)
                .build();
    }

    private TrainInfo getTrainsInfo(Departure departure) {
        return new TrainInfo(departure.direction(),
                departure.actualDateTime().substring(11, 16),
                departure.actualTrack(), departure.trainCategory(),
                getRouteStations(departure.routeStations()),
                departure.departureStatus(), departure.cancelled());
    }

    private String getRouteStations(List<RouteStation> routeStations) {
        return routeStations.stream().map(RouteStation::mediumName)
                .collect(Collectors.joining(", "));
    }

}
