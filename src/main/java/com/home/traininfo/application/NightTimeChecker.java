package com.home.traininfo.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.function.Supplier;

@Component
public class NightTimeChecker {

    private final boolean nightMode;
    private final Supplier<LocalTime> currentTimeSupplier;


    public NightTimeChecker(@Value("${nightMode}") boolean nightMode,
                            Supplier<LocalTime> currentTimeSupplier) {
        this.nightMode = nightMode;
        this.currentTimeSupplier = currentTimeSupplier;
    }

    public boolean isNightTime() {
        return nightMode && isTimeBetween1AMAnd5AM();
    }

    private boolean isTimeBetween1AMAnd5AM() {
        LocalTime startTime = LocalTime.of(1, 0);
        LocalTime endTime = LocalTime.of(5, 0);
        LocalTime currentTime = currentTimeSupplier.get();
        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }
}
