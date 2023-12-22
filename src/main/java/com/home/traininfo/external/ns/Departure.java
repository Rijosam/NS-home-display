package com.home.traininfo.external.ns;

import java.util.List;


public record Departure(
        String direction,
        String name,
        String plannedDateTime,
        Integer plannedTimeZoneOffset,
        String actualDateTime,
        String plannedTrack,
        String actualTrack,
        Product product,
        String trainCategory,
        Boolean cancelled,
        String journeyDetailRef,
        List<RouteStation> routeStations,
        List<Message> messages,
        String departureStatus) {
}


  


  