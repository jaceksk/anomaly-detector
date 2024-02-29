package com.art.anomaly.detector.core.stream;

import java.util.Objects;

import com.art.anomaly.detector.core.stream.service.AnomalyDetector;
import lombok.RequiredArgsConstructor;
import com.art.anomaly.detector.core.stream.model.MeasurementsData;
import com.art.anomaly.detector.core.stream.model.TemperatureMeasurementMessage;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AnomalyDetectorService {
    private final AnomalyDetector anomalyDetector;
    public MeasurementsData apply(TemperatureMeasurementMessage currentMeasurementMessage, MeasurementsData measurements) {
        var currentMeasurement = currentMeasurementMessage.convert();
        measurements.setRoomId(currentMeasurement.getThermometerId());
        measurements.removeUnnecessaryMeasurements(currentMeasurement.getTime());


        var newAnomalies = anomalyDetector.calculateAnomaly(currentMeasurement, measurements);

        measurements.add(currentMeasurement);

        if (Objects.nonNull(newAnomalies) && !newAnomalies.isEmpty()) {
            measurements.addAnomaly(newAnomalies);
        }
        return measurements;
    }

}
