package com.art.anomaly.detector.core.stream.service;

import com.art.anomaly.detector.core.stream.model.Measurement;
import com.art.anomaly.detector.core.stream.model.MeasurementsData;

import java.util.List;
import java.util.stream.Collectors;

public class RangeAnomalyDetector implements AnomalyDetector {
    @Override
    public List<Measurement> calculateAnomaly(Measurement current, MeasurementsData data) {
        if (!shouldCalculateAnomaly(data)) {
            return List.of();
        }

        var average = data.averageOfLastMeasurements();
        var anomalyMeasument = data.getMeasurements().stream()
                .filter(x -> (average + 5 < x.getTemperature()) || (average - 5 > x.getTemperature()))
                .collect(Collectors.toList());

        if (average + 5 < current.getTemperature() || (average - 5 > current.getTemperature())) {
            anomalyMeasument.add(current);
        }

        return anomalyMeasument;
    }

    private boolean shouldCalculateAnomaly(MeasurementsData measurementsData) {
        return measurementsData.getMeasurements().size() > 9;
    }
}
