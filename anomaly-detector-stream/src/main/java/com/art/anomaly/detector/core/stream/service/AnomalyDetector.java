package com.art.anomaly.detector.core.stream.service;

import com.art.anomaly.detector.core.stream.model.Measurement;
import com.art.anomaly.detector.core.stream.model.MeasurementsData;

import java.util.List;

public interface AnomalyDetector {

    List<Measurement> calculateAnomaly(Measurement current, MeasurementsData data);
}
