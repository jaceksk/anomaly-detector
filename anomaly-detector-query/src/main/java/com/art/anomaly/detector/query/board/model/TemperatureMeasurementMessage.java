package com.art.anomaly.detector.query.board.model;

import java.time.Instant;
import java.util.UUID;

public class TemperatureMeasurementMessage {
    private UUID id;
    private UUID roomID;
    private UUID thermometerId;
    private double temperature;
    private Long time;

    public TemperatureMeasurementMessage(
            UUID id,
            UUID roomID,
            UUID thermometerId,
            double temperature,
            Instant time
    ) {
        this.id = id;
        this.roomID = roomID;
        this.thermometerId = thermometerId;
        this.temperature = temperature;
        this.time = time.getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public UUID getRoomID() {
        return roomID;
    }

    public UUID getThermometerId() {
        return thermometerId;
    }
}
