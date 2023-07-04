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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TrainInfoProvider implements TrainInfoProviderClient {

    private static final Jsonb jsonb = JsonbBuilder.create();

    @Value("${UriPath}")
    private String UriPath;
    @Value("${BaseUrl}")
    private String BaseUrl;
    @Value("${SubscriptionKey}")
    private String SubscriptionKey;

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

        } catch (ProcessingException e) {
           throw e;
        }
        return null;
    }

    private DeparturesPayload getDeparturesPayload(Response response) {
        return jsonb.fromJson(response.readEntity(String.class), DeparturesPayload.class);
    }

    private Response getTrainsInfo(final String stationUicCode) {
        return getWebTarget(stationUicCode)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header("Ocp-Apim-Subscription-Key", SubscriptionKey)
                .buildGet()
                .invoke();
    }

    private WebTarget getWebTarget(String stationUicCode) {
        return ClientBuilder.newBuilder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .build()
                .target(getUri(stationUicCode));

    }

    private URI getUri(String stationUicCode) {
        return UriBuilder
                .fromUri(BaseUrl).path(UriPath)
                .queryParam("uicCode", stationUicCode)
                .queryParam("maxJourneys", 40)
                .build();
    }

    private TrainInfo getTrainsInfo(Departure departure) {
        return new TrainInfo(departure.direction(), departure.actualDateTime().substring(11, 16),
                departure.actualTrack(), departure.trainCategory(), departure.cancelled());
    }



}
