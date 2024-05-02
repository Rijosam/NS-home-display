package com.home.traininfo.admin;

import java.util.Arrays;

public enum Station {
    TIEL("Tiel","8400596"),
    AMSTERDAM("Amsterdam C","8400058"),
    ROTTERDAM("Rotterdam C","8400530"),
    EINDHOVEN("Eindhoven C","8400206"),
    AMSTERDAM_ZUID("Amsterdam Zuid","8400061"),
    UTRECHT("Utrecht","8400621");

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
