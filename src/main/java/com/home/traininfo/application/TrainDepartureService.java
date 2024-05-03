package com.home.traininfo.application;

import com.home.traininfo.domain.TrainDeparture;

import java.util.List;

public interface TrainDepartureService {

    List<TrainDeparture> getDepartureInfo();
    void setStationUicCode(String stationUicCode);
    String getStationUicCode();
}

