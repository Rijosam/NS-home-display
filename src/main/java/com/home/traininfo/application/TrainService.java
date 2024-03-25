package com.home.traininfo.application;

import com.home.traininfo.domain.TrainDeparture;
import com.home.traininfo.external.TrainInfo;
import com.home.traininfo.external.TrainInfoProviderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrainService implements TrainDepartureService {

    private final String stationUicCode;
    private final TrainInfoProviderClient infoProvider;

    public TrainService(@Value("${stationUicCode}") String stationUicCode,
                        TrainInfoProviderClient infoProvider) {
        this.stationUicCode = stationUicCode;
        this.infoProvider = infoProvider;
    }

    @Override
    public List<TrainDeparture> getDepartureInfo() {
        var departureTrains = infoProvider.getDepartureTrains(stationUicCode);
        return departureTrains.stream().map(this::getTrainDeparture).toList();
    }

    private TrainDeparture getTrainDeparture(TrainInfo trainInfo) {
        return new TrainDeparture(
                trainInfo.direction(), trainInfo.actualDepartureTime(),
                trainInfo.actualTrack(), trainInfo.trainCategory(),
                trainInfo.routeStations(), trainInfo.getStatus(),
                trainInfo.getFormattedTimeToDeparture());
    }
}
