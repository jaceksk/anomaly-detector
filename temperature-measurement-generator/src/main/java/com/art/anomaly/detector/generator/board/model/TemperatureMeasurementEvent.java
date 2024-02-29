package com.art.anomaly.detector.generator.board.model;

import java.time.Instant;
import java.util.UUID;

public class TemperatureMeasurementEvent {
    private UUID roomID;
    private UUID thermometerId;
    private double temperature;
    private Instant time;

    public TemperatureMeasurementEvent(UUID roomID, UUID thermometerId, double temperature, Instant time) {
        this.roomID = roomID;
        this.thermometerId = thermometerId;
        this.temperature = temperature;
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public Instant getTime() {
        return time;
    }

    public UUID getRoomId() {
        return roomID;
    }

    public UUID getThermometerId() {
        return thermometerId;
    }

    public TemperatureMeasurementMessage toRoomMessage() {
        return new TemperatureMeasurementMessage(roomID, roomID, thermometerId, temperature, time);
    }

    public TemperatureMeasurementMessage toThermometerMessage() {
        return new TemperatureMeasurementMessage(thermometerId, roomID, thermometerId, temperature, time);
    }
}
