package com.example.planefinder_api;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@AllArgsConstructor
@Configuration
public class PositionReporter {
    private final AircraftController aircraftController;

    @Bean
    Supplier<Flux<Aircraft>> reportPositions() {
        return () -> aircraftController.getAircraft()
                .onErrorResume(e -> {
                    System.err.println("Error retrieving aircraft: " + e.getMessage());
                    return Flux.empty();
                });
    }
}
