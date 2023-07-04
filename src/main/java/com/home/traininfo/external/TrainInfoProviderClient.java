package com.home.traininfo.external;

import java.util.List;

public interface TrainInfoProviderClient {
    List<TrainInfo> getDepartureTrains(final String stationUicCode);
}
