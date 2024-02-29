package com.art.anomaly.detector.core.stream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    private UUID thermometerId;
    private double temperature;
    private Instant time;
    private boolean hasAnyAnomaly;

    public Measurement(UUID thermometerId, double temperature, Instant time) {
        this.temperature = temperature;
        this.time = time;
        this.hasAnyAnomaly = false;
    }

    public void markAsWithAnomaly() {
        this.hasAnyAnomaly = true;
    }
}
