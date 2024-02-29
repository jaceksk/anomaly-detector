package com.art.anomaly.detector.core.stream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementAnomaly {
    private UUID roomId;
    private UUID thermometerId;
    private double temperature;
    private Instant time;
}
