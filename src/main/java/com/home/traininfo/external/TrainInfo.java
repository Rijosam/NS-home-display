package com.home.traininfo.external;

public record TrainInfo(String direction, String actualDepartureTime, String actualTrack, String trainCategory,
                        Boolean isCancelled) {
}
