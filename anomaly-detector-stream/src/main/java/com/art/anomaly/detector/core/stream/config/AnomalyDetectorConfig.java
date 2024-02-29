package com.art.anomaly.detector.core.stream.config;

import com.art.anomaly.detector.core.stream.service.AnomalyDetector;
import com.art.anomaly.detector.core.stream.service.RangeAnomalyDetector;
import com.art.anomaly.detector.core.stream.service.TimeAnomalyDetector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnomalyDetectorConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "detector", havingValue = "range")
    public AnomalyDetector rangeAnomalyDetector() {
        return new RangeAnomalyDetector();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "detector", havingValue = "time")
    public AnomalyDetector TimeAnomalyDetector() {
        return new TimeAnomalyDetector();
    }
}
