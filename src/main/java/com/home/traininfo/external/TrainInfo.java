package com.home.traininfo.external;

import com.home.traininfo.domain.Status;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalTime;

public record TrainInfo(String direction,
                        String plannedDepartureTime,
                        String actualDepartureTime,
                        String actualTrack,
                        String trainCategory,
                        String routeStations,
                        String departureStatus,
                        boolean isCancelled) {

    public Status getStatus(){
       return isCancelled ? Status.CANCELLED : Status.getStatus(departureStatus);
    }

    public String getFormattedTimeToDeparture() {
        var timeToDepartureDuration = Duration.between(LocalTime.now(),
                LocalTime.parse(actualDepartureTime));
        var hours = timeToDepartureDuration.toHours();
        var minutes = timeToDepartureDuration.toMinutesPart();

        if (hours == 0 && minutes == 0) {
            return "<1 min";
        } else if (hours == 0) {
            return minutes + " min";
        } else if (minutes == 0) {
            return hours + " hr";
        } else {
            return hours + " hr " + minutes + " min";
        }
    }

    public String getDepartureDelayInMinutes() {
        if (plannedDepartureTime.equals(actualDepartureTime)) {
            return StringUtils.EMPTY;
        } else {
            var departureDelay = Duration.between(LocalTime.parse(plannedDepartureTime),
                    LocalTime.parse(actualDepartureTime));
            return String.valueOf(departureDelay.toMinutes());
        }
    }
}
