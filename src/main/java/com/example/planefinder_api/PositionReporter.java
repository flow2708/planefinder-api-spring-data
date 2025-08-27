package com.example.planefinder_api;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor
@Configuration
public class PositionReporter {
    private final AircraftController aircraftController;

    @Bean
    Supplier<Iterable<Aircraft>> reportPositions() {
        return () -> {
            try {
                return aircraftController.getAircraft();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return List.of();
        };
    }
}
