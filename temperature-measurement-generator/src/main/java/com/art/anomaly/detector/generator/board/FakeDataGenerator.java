package com.art.anomaly.detector.generator.board;

import com.art.anomaly.detector.generator.board.model.TemperatureMeasurementEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class FakeDataGenerator {
    private final KafkaProducer producer;

    public FakeDataGenerator(KafkaProducer producer) {
        this.producer = producer;
    }

    @PostConstruct
    public void generate() {
        var random = new Random();
        var roomUuids = IntStream.range(0, 200).boxed().map(x -> UUID.randomUUID()).toList();
        var thermometerUuids = IntStream.range(0, 200).boxed().map(x -> UUID.randomUUID()).toList();

        System.out.println("Start generate messages");

        IntStream.range(0, 20000)
                .forEach(x ->
                        producer.publish(
                                new TemperatureMeasurementEvent(
                                        roomUuids.get(random.nextInt(199)),
                                        thermometerUuids.get(random.nextInt(199)),
                                        20 + random.nextDouble(20),
                                        Instant.now()
                                )
                        )
                );

        System.out.println("Finish generate messages");
    }
}
