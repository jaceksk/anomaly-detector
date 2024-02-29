package com.art.anomaly.detector.core.stream.model;

import lombok.Getter;

import java.time.Instant;
import java.util.*;


@Getter
public class MeasurementsData {
    private UUID roomId;
    private List<MeasurementAnomaly> currentAnomaly = new ArrayList<>();
    private List<Measurement> measurements = new ArrayList<>();

    public MeasurementsData() {
        this.measurements = new ArrayList<>();
    }

    public void add(Measurement measurement) {
        this.measurements.add(measurement);
    }

    public void addAnomaly(List<Measurement> anomaly) {
        var newAnomaly = anomaly.stream()
                .filter(measurement -> !measurement.isHasAnyAnomaly())
                .map(x -> new MeasurementAnomaly(roomId, x.getThermometerId(), x.getTemperature(), x.getTime())).toList();

        anomaly.forEach(Measurement::markAsWithAnomaly);

        this.currentAnomaly.addAll(newAnomaly);
    }

    public void removeUnnecessaryMeasurements(Instant processTime) {
        this.currentAnomaly.clear();

        Measurement oldestMeasurement = findOldestMeasurement();
        while (oldestMeasurement != null && measurements.size() > 10 && oldestMeasurement.getTime().isBefore(processTime.minusSeconds(10))) {
            measurements.remove(0);
            oldestMeasurement = findOldestMeasurement();
        }
    }

    public double averageOfLastMeasurements() {
        return measurements.stream().mapToDouble(Measurement::getTemperature).average().orElseThrow();
    }

    public Double getMinMeasureWithoutAnomaly(Instant after) {
        return measurements.stream()
                .filter(x -> x.getTime().isAfter(after))
                .filter(x -> !x.isHasAnyAnomaly())
                .sorted(Comparator.comparing(Measurement::getTemperature))
                .map(Measurement::getTemperature)
                .findFirst()
                .orElse(null);
    }
    public Double getMaxMeasureWithoutAnomaly(Instant after) {
        return measurements.stream()
                .filter(x -> x.getTime().isAfter(after))
                .filter(x -> !x.isHasAnyAnomaly())
                .sorted(Comparator.comparing(Measurement::getTemperature))
                .map(Measurement::getTemperature)
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public boolean hasAnyAnomaly() {
        return !currentAnomaly.isEmpty();
    }

    private Measurement findOldestMeasurement() {
        measurements.sort(Comparator.comparing(Measurement::getTime));
        return !measurements.isEmpty() ? measurements.get(0) : null;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }
}
