package com.home.traininfo.domain;

public record TrainDeparture(String direction, String actualDepartureTime,
                             String actualTrack, String trainCategory,
                             String routeStations, Status status,
                             String timeToDeparture) {
}
