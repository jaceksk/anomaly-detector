package com.art.anomaly.detector.core.stream.config;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import com.art.anomaly.detector.core.stream.model.TemperatureMeasurementMessage;
import com.art.anomaly.detector.core.stream.model.MeasurementsData;
import org.springframework.kafka.support.serializer.JsonSerde;

public class SerdeConstants {

    public static final Serde<TemperatureMeasurementMessage> MEASUREMENT_DATA_SERDE = new JsonSerde<>(TemperatureMeasurementMessage.class);

    public static final Serde<MeasurementsData> MEASUREMENTS_DATA_SERDE = new JsonSerde<>(MeasurementsData.class);

    public static final KeyValueBytesStoreSupplier STORE_SUPPLIER = Stores.inMemoryKeyValueStore("MeasurementsData");

}
