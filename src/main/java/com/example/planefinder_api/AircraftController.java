package com.example.planefinder_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Aircraft> getAircraft() {
        Aircraft ar1 = generateRandomAircraft();
        Aircraft ar2 = generateRandomAircraft();

        return List.of(ar1, ar2);
    }
    private Aircraft generateRandomAircraft() {
        Aircraft ac = new Aircraft();
        ac.setId(random.nextLong());
        ac.setCallsign(callsigns.get(random.nextInt(callsigns.size())));
        ac.setSquawk(String.valueOf(random.nextInt(9999)));
        ac.setReg("REG" + random.nextInt(1000));
        ac.setAltitude(random.nextInt(40000));
        ac.setSpeed(random.nextInt(600));
        ac.setLat(random.nextDouble() * 180 - 90);  // Широта (-90..90)
        ac.setLon(random.nextDouble() * 360 - 180); // Долгота (-180..180)
        ac.setLastSeenTime(String.valueOf(Instant.now()));
        ac.setPosUpdateTime(String.valueOf(Instant.now()));
        ac.setBds40SeenTime(String.valueOf(Instant.now()));

        return ac;
    }
}


