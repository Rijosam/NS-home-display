package com.home.traininfo.external;

import com.home.traininfo.domain.Status;

public record TrainInfo(String direction,
                        String actualDepartureTime,
                        String actualTrack,
                        String trainCategory,
                        String routeStations,
                        String departureStatus,
                        boolean isCancelled) {

    public Status getStatus(){
       return isCancelled ? Status.CANCELLED : Status.getStatus(departureStatus);
    }
}
