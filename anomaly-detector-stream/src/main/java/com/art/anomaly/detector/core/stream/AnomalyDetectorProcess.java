package com.art.anomaly.detector.core.stream;

import com.art.anomaly.detector.core.stream.config.SerdeConstants;
import com.art.anomaly.detector.core.stream.model.MeasurementAnomaly;
import com.art.anomaly.detector.core.stream.model.MeasurementsData;
import com.art.anomaly.detector.core.stream.model.TemperatureMeasurementMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class AnomalyDetectorProcess {
    private final AnomalyDetectorService anomalyDetector;

    @Bean
    public Function<KStream<String, TemperatureMeasurementMessage>, KStream<String, MeasurementAnomaly>> process() {
        return input -> input
                .groupByKey(Grouped.with(Serdes.String(), SerdeConstants.MEASUREMENT_DATA_SERDE))
                .aggregate(MeasurementsData::new,
                        (key, updateEvent, summaryEvent) -> anomalyDetector.apply(updateEvent, summaryEvent),
                        Materialized.<String, MeasurementsData>as(SerdeConstants.STORE_SUPPLIER)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(SerdeConstants.MEASUREMENTS_DATA_SERDE))
                .toStream()
                .filter((k, v) -> v.hasAnyAnomaly())
                .flatMapValues((k, v) -> v.getCurrentAnomaly());
    }
}