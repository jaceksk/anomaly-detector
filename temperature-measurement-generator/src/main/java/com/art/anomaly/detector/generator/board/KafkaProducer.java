package com.art.anomaly.detector.generator.board;

import com.art.anomaly.detector.generator.board.model.TemperatureMeasurementEvent;
import com.art.anomaly.detector.generator.board.model.TemperatureMeasurementMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${app.kafka.room-topic-name}")
    private String roomTopicName;
    @Value("${app.kafka.thermometer-topic-name}")
    private String thermometerTopicName;
    private final KafkaTemplate<String, TemperatureMeasurementMessage> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, TemperatureMeasurementMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TemperatureMeasurementEvent message) {
        var roomMessage = new ProducerRecord<>(roomTopicName, message.getRoomId().toString(), message.toRoomMessage());
        roomMessage.headers().remove("__TypeId__");
        kafkaTemplate.send(roomMessage);


        var thermometerMessage = new ProducerRecord<>(thermometerTopicName, message.getThermometerId().toString(), message.toThermometerMessage());
        thermometerMessage.headers().remove("__TypeId__");
        kafkaTemplate.send(thermometerMessage);
    }
}
