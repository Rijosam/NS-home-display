package com.home.traininfo.domain;

import java.util.stream.Stream;

public enum Status {
    ON_STATION,
    CANCELLED,
    DEPARTED,
    INCOMING;

    public static Status getStatus(String status) {
        return Stream.of(values())
                .filter(value -> value.name().equalsIgnoreCase(status))
                .findAny().orElse(Status.INCOMING);
    }
}
