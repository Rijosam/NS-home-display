package com.home.traininfo.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.function.Supplier;

@Configuration
public class CurrentTimeSupplierConfig {

    @Bean
    public Supplier<LocalTime> currentTimeSupplier() {
        return LocalTime::now;
    }
}
