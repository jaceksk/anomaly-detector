package com.art.anomaly.detector.core.stream.service;

import com.art.anomaly.detector.core.stream.model.Measurement;
import com.art.anomaly.detector.core.stream.model.MeasurementsData;

import java.util.List;
import java.util.Objects;

public class TimeAnomalyDetector implements AnomalyDetector {

    @Override
    public List<Measurement> calculateAnomaly(Measurement current, MeasurementsData data) {
        if (!shouldCalculateAnomaly(data)) {
            return List.of();
        }

        var min = data.getMinMeasureWithoutAnomaly(current.getTime().minusSeconds(100));
        var max = data.getMaxMeasureWithoutAnomaly(current.getTime().minusSeconds(100));

        return (Objects.nonNull(min) && min + 5 <= current.getTemperature()) || (Objects.nonNull(max) && max - 5 >= current.getTemperature())
                ? List.of(current)
                : List.of();
    }

    private boolean shouldCalculateAnomaly(MeasurementsData measurementsData) {
        return measurementsData.getMeasurements().size() > 1;
    }
}
