package com.art.anomaly.detector.core.fixtures;

import com.art.anomaly.detector.core.stream.model.TemperatureMeasurementMessage;

import java.time.Instant;
import java.util.UUID;

public class RoomTemperatureMeasurementMessageFixtures {
    public static final UUID ROOM_ID = UUID.randomUUID();

    public static TemperatureMeasurementMessage of(double temperature) {
        return new TemperatureMeasurementMessage(ROOM_ID, temperature, Instant.now());
    }

    public static TemperatureMeasurementMessage of(double temperature, Instant date) {
        return new TemperatureMeasurementMessage(ROOM_ID, temperature, date);
    }

    public static TemperatureMeasurementMessage of(double temperature, Long date) {
        return new TemperatureMeasurementMessage(ROOM_ID, temperature, Instant.ofEpochMilli(date));
    }
}
