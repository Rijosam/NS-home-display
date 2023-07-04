package com.home.traininfo.domain;

public record TrainDeparture(String direction, String actualDepartureTime, String actualTrack, String trainCategory,
                             Boolean isCancelled) {
}
