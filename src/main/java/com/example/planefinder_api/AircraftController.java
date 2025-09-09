package com.example.planefinder_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {
    private final Random random = new Random();
    private final List<String> callsigns = Arrays.asList("DAL123", "UAL456", "AAL789", "RYR555");

    @GetMapping
    public Flux<Aircraft> getAircraft() {
        // Генерация асинхронно с задержкой для имитации реального запроса
        return Flux.interval(Duration.ofMillis(100)) // Генерируем данные каждые 100ms
                .take(2) // Берем только 2 самолета
                .flatMap(i -> generateRandomAircraftAsync()
                                .subscribeOn(Schedulers.boundedElastic()), // Выполняем в отдельном потоке
                        1) // Ограничиваем параллелизм
                .onErrorResume(e -> {
                    System.err.println("Error generating aircraft: " + e.getMessage());
                    return Flux.empty();
                });
    }

    // Асинхронная версия генерации самолета
    private Mono<Aircraft> generateRandomAircraftAsync() {
        return Mono.fromCallable(() -> {
            // Имитация задержки сети/обработки
            try {
                Thread.sleep(random.nextInt(50)); // Случайная задержка 0-50ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Aircraft ac = new Aircraft();
            ac.setId(random.nextLong());
            ac.setCallsign(callsigns.get(random.nextInt(callsigns.size())));
            ac.setSquawk(String.valueOf(random.nextInt(9999)));
            ac.setReg("REG" + random.nextInt(1000));
            ac.setAltitude(random.nextInt(40000));
            ac.setSpeed(random.nextInt(600));
            ac.setLat(random.nextDouble() * 180 - 90);
            ac.setLon(random.nextDouble() * 360 - 180);
            ac.setLastSeenTime(Instant.now());
            ac.setPosUpdateTime(Instant.now());
            ac.setBds40SeenTime(Instant.now());

            return ac;
        });
    }

    // Дополнительный endpoint для потоковой передачи данных
    @GetMapping("/stream")
    public Flux<Aircraft> getAircraftStream() {
        return Flux.interval(Duration.ofSeconds(1)) // Новый самолет каждую секунду
                .flatMap(i -> generateRandomAircraftAsync()
                        .subscribeOn(Schedulers.boundedElastic()))
                .onErrorResume(e -> {
                    System.err.println("Error in aircraft stream: " + e.getMessage());
                    return Flux.empty();
                });
    }
}