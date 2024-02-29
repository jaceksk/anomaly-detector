package com.art.anomaly.detector.core.stream.config;

import com.art.anomaly.detector.core.stream.AnomalyDetectorService;
import com.art.anomaly.detector.core.stream.model.MeasurementAnomaly;
import com.art.anomaly.detector.core.stream.service.AnomalyDetector;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import com.art.anomaly.detector.core.stream.AnomalyDetectorProcess;
import com.art.anomaly.detector.core.stream.model.TemperatureMeasurementMessage;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.*;

public class RoomMeasurementTestTopologyDriver {
    static final String INPUT_TOPIC = "room-measurement";
    static final String OUTPUT_TOPIC = "room-anomaly";

    private Serde<MeasurementAnomaly> countEventSerde = new JsonSerde<>(MeasurementAnomaly.class);
    private Serde<TemperatureMeasurementMessage> updateEventSerde = new JsonSerde<>(TemperatureMeasurementMessage.class);
    private Serde<String> keySerde = Serdes.String();
    private final TopologyTestDriver testDriver;
    private TestInputTopic<String, TemperatureMeasurementMessage> inputTopic;
    private TestOutputTopic<String, MeasurementAnomaly> outputTopic;

    public RoomMeasurementTestTopologyDriver(AnomalyDetector anomalyDetector) {
        configureDeserializer(countEventSerde.deserializer(), String.class, MeasurementAnomaly.class, false);
        configureDeserializer(keySerde.deserializer(), String.class, null, true);

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, TemperatureMeasurementMessage> input = builder.stream(INPUT_TOPIC, Consumed.with(keySerde, updateEventSerde));
        AnomalyDetectorProcess inventoryAggregator = new AnomalyDetectorProcess(new AnomalyDetectorService(anomalyDetector));

        KStream<String, MeasurementAnomaly> output = inventoryAggregator.process().apply(input);
        output.to(OUTPUT_TOPIC);

        Topology topology = builder.build();
        testDriver = new TopologyTestDriver(topology, getStreamsConfiguration());
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, keySerde.serializer(), updateEventSerde.serializer());
        outputTopic = testDriver.createOutputTopic(OUTPUT_TOPIC, keySerde.deserializer(), countEventSerde.deserializer());
    }

    public void close() {
        try {
            testDriver.close();
        } catch (final RuntimeException e) {
            System.out.println("Ignoring exception, test failing in Windows due this exception:" + e.getLocalizedMessage());
        }
    }

    public void publish(String key, TemperatureMeasurementMessage message) {
        inputTopic.pipeInput(key, message);
    }

    public void publish(TemperatureMeasurementMessage message) {
        publish(message.getId().toString(), message);
    }

    public boolean isOutputTopicEmpty() {
        return outputTopic.isEmpty();
    }

    public List<MeasurementAnomaly> loadOutputMessages(int expectedCount) {
        List<MeasurementAnomaly> inventoryCountEvents = new LinkedList<>();
        int receivedCount = 0;
        while (receivedCount < expectedCount) {
            MeasurementAnomaly record = outputTopic.readValue();
            if (record == null) {
                break;
            }
            receivedCount++;
            inventoryCountEvents.add(record);
        }
        return inventoryCountEvents;
    }

    static Properties getStreamsConfiguration() {
        final Properties streamsConfiguration = new Properties();
        // Need to be set even these do not matter with TopologyTestDriver
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "TopologyTestDriver");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "ignored");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());

        return streamsConfiguration;
    }

    private void configureDeserializer(Deserializer<?> deserializer, Class<?> keyDefaultType, Class<?> valueDefaultType, boolean isKey) {
        Map<String, Object> deserializerConfig = new HashMap<>();
        deserializerConfig.put(JsonDeserializer.KEY_DEFAULT_TYPE, keyDefaultType);
        deserializerConfig.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueDefaultType);
        deserializer.configure(deserializerConfig, isKey);
    }

}
