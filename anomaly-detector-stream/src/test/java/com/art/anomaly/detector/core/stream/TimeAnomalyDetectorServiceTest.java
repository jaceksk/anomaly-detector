package com.art.anomaly.detector.core.stream;

import com.art.anomaly.detector.core.stream.config.RoomMeasurementTestTopologyDriver;
import com.art.anomaly.detector.core.stream.model.TemperatureMeasurementMessage;
import com.art.anomaly.detector.core.stream.service.TimeAnomalyDetector;
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

public class TimeAnomalyDetectorServiceTest {

    private RoomMeasurementTestTopologyDriver testDriver;

    @BeforeEach
    void setup() {
        testDriver = new RoomMeasurementTestTopologyDriver(new TimeAnomalyDetector());
        Assertions.assertThat(testDriver.isOutputTopicEmpty()).isTrue();
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void testCases(List<Double> expectedAnomaly, List<TemperatureMeasurementMessage> sampleData) throws InterruptedException {
        sampleData.forEach(message -> testDriver.publish(message));

        Thread.sleep(1000);

        var anomalies = testDriver.loadOutputMessages(expectedAnomaly.size());
        var anomaliesTemperature = MeasurementAnomalyUtils.getAsDouble(anomalies);

        Assertions.assertThat(anomaliesTemperature).containsExactlyInAnyOrderElementsOf(expectedAnomaly);
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(List.of(24.1, 23.4),
                        List.of(
                                RoomTemperatureMeasurementMessageFixtures.of(19.1, 1684945005L),
                                RoomTemperatureMeasurementMessageFixtures.of(19.2, 1684945006L),
                                RoomTemperatureMeasurementMessageFixtures.of(19.5, 1684945007L),
                                RoomTemperatureMeasurementMessageFixtures.of(19.7, 1684945008L),
                                RoomTemperatureMeasurementMessageFixtures.of(19.3, 1684945009L),
                                RoomTemperatureMeasurementMessageFixtures.of(24.1, 1684945010L),
                                RoomTemperatureMeasurementMessageFixtures.of(18.2, 1684945011L),
                                RoomTemperatureMeasurementMessageFixtures.of(19.1, 1684945012L),
                                RoomTemperatureMeasurementMessageFixtures.of(19.2, 1684945013L),
                                RoomTemperatureMeasurementMessageFixtures.of(23.4, 1684945015L)
                        )
                )
        );
    }
}