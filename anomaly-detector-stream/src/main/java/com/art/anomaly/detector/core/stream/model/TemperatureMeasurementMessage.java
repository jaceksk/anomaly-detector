package com.art.anomaly.detector.core.stream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureMeasurementMessage {
    private UUID id;
    private UUID roomID;
    private UUID thermometerId;
    private double temperature;
    private Instant time;

    public Measurement convert() {
        return new Measurement(thermometerId, temperature, time);
    }
}
