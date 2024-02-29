package com.art.anomaly.detector.core.stream;

import com.art.anomaly.detector.core.stream.config.RoomMeasurementTestTopologyDriver;
import com.art.anomaly.detector.core.stream.service.RangeAnomalyDetector;
import com.art.anomaly.detector.core.utils.MeasurementAnomalyUtils;
import org.assertj.core.api.Assertions;
import com.art.anomaly.detector.core.fixtures.RoomTemperatureMeasurementMessageFixtures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;



public class RangeAnomalyDetectorServiceTest {
    private RoomMeasurementTestTopologyDriver testDriver;

    @BeforeEach
    void setup() {
        testDriver = new RoomMeasurementTestTopologyDriver(new RangeAnomalyDetector());
        Assertions.assertThat(testDriver.isOutputTopicEmpty()).isTrue();
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void testCases(List<Double> expectedAnomaly, List<Double> sampleData) throws InterruptedException {
        sampleData.forEach(temperature -> testDriver.publish(RoomTemperatureMeasurementMessageFixtures.of(temperature)));

        Thread.sleep(1000);

        var anomalies = testDriver.loadOutputMessages(expectedAnomaly.size());
        var anomaliesTemperature = MeasurementAnomalyUtils.getAsDouble(anomalies);

        Assertions.assertThat(anomaliesTemperature).containsExactlyInAnyOrderElementsOf(expectedAnomaly);
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(List.of(27.1), List.of(20.1, 21.2, 20.3, 19.1, 20.1, 19.2, 20.1, 18.1, 19.4, 20.1, 27.1, 23.1)),
                Arguments.of(List.of(25.8, 26.1), List.of(19.1, 19.1, 19.1, 19.2, 19.5, 19.7, 19.3, 18.2, 19.1, 19.2, 25.8, 26.1))
        );
    }
}