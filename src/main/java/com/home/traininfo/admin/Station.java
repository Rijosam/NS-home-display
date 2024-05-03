package com.home.traininfo.admin;

import java.util.Arrays;

public enum Station {
    AMSTERDAM("Amsterdam Centraal","8400058"),
    AMSTERDAM_ZUID("Amsterdam Zuid","8400061"),
    ARNHEM_CENTRAL("Arnhem Centraal","8400071"),
    EINDHOVEN("Eindhoven Centraal","8400206"),
    ROTTERDAM("Rotterdam Centraal","8400530"),
    TIEL("Tiel","8400596"),
    UTRECHT("Utrecht Centraal","8400621"),
    ZAANDAM("Zaandam","8400731");

    private final String displayName;
    private final String stationCode;

    Station(String displayName, String stationCode) {
        this.displayName = displayName;
        this.stationCode = stationCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public static String getStationCodeFromDisplayName(String displayName) {
        Station station = Arrays.stream(Station.values())
                .filter(value -> value.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum found with display name: " + displayName));
        return station.getStationCode();
    }
}
