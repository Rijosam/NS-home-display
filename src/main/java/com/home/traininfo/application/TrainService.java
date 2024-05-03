package com.home.traininfo.application;

import com.home.traininfo.domain.TrainDeparture;
import com.home.traininfo.external.TrainInfo;
import com.home.traininfo.external.TrainInfoProviderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TrainService implements TrainDepartureService {

    private String stationUicCode;
    private final TrainInfoProviderClient infoProvider;
    private final NightTimeChecker nightTimeChecker;


    public TrainService(@Value("${stationUicCode}") String stationUicCode,
                        TrainInfoProviderClient infoProvider,
                        NightTimeChecker nightTimeChecker) {
        this.stationUicCode = stationUicCode;
        this.infoProvider = infoProvider;
        this.nightTimeChecker = nightTimeChecker;
    }

    @Override
    public List<TrainDeparture> getDepartureInfo() {
        if (nightTimeChecker.isNightTime()){
            return Collections.emptyList();
        }
        var departureTrains = infoProvider.getDepartureTrains(stationUicCode);
        return departureTrains.stream().map(this::getTrainDeparture).toList();
    }

    private TrainDeparture getTrainDeparture(TrainInfo trainInfo) {
        return new TrainDeparture(
                trainInfo.direction(), trainInfo.plannedDepartureTime(),
                trainInfo.actualTrack(), trainInfo.trainCategory(),
                trainInfo.routeStations(), trainInfo.getStatus(),
                trainInfo.getFormattedTimeToDeparture(),
                trainInfo.getDepartureDelayInMinutes());
    }

    @Override
    public void setStationUicCode(String stationUicCode) {
        this.stationUicCode = stationUicCode;
    }

    @Override
    public String getStationUicCode() {
        return stationUicCode;
    }
}
