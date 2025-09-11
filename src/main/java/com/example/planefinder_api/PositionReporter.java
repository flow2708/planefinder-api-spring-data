package com.example.planefinder_api;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Supplier;

@AllArgsConstructor
@Configuration
public class PositionReporter {
    private final AircraftController aircraftController;

    @Bean
    Supplier<Flux<Aircraft>> reportPositions() {
        return () -> Flux.interval(Duration.ofSeconds(5)) // Отчет каждые 5 секунд
                .Map(i -> aircraftController.getAircraft()
                        .onErrorResume(e -> {
                            System.err.println("Error retrieving aircraft: " + e.getMessage());
                            return Flux.empty();
                        }))
                .doOnNext(aircraft ->
                        System.out.println("Reporting aircraft: " + aircraft.getCallsign()))
                .doOnError(e ->
                        System.err.println("Critical error in position reporter: " + e.getMessage()));
    }

    // Дополнительный бин для непрерывного потока данных
    @Bean
    Supplier<Flux<Aircraft>> continuousPositionStream() {
        return () -> aircraftController.getAircraftStream()
                .onErrorResume(e -> {
                    System.err.println("Error in continuous stream: " + e.getMessage());
                    return Flux.empty();
                });
    }
}