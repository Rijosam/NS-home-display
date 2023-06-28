package com.home.application.model;

public record TrainInfo(String direction, String actualDepartureTime, String actualTrack, String trainCategory,
                        Boolean isCancelled) {
}
