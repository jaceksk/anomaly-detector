package com.art.anomaly.detector.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.art.anomaly.detector.core.stream.model.MeasurementAnomaly;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeasurementAnomalyUtils {

    public static List<Double> getAsDouble(List<MeasurementAnomaly> anomalies) {
        return anomalies.stream().mapToDouble(MeasurementAnomaly::getTemperature).boxed().toList();
    }
}
