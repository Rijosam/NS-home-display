package com.home.traininfo.application;

import com.home.traininfo.domain.TrainDeparture;
import com.home.traininfo.external.TrainInfo;
import com.home.traininfo.external.TrainInfoProviderClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService implements TrainDepartureService {

    private final TrainInfoProviderClient infoProvider;

    public TrainService(TrainInfoProviderClient infoProvider) {
        this.infoProvider = infoProvider;
    }


    @Override
    public List<TrainDeparture> getDepartureInfoService() {
        String UicCodeTiel = "8400596";
        var departureTrains = infoProvider.getDepartureTrains(UicCodeTiel);
        return departureTrains.stream().map(this::getTrainDeparture).toList();
    }

    private TrainDeparture getTrainDeparture(TrainInfo trainInfo) {
        return new TrainDeparture(trainInfo.direction(), trainInfo.actualDepartureTime(),
                trainInfo.actualTrack(), trainInfo.trainCategory(), trainInfo.isCancelled());
    }
}
