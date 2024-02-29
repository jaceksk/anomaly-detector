package com.art.anomaly.detector.query.board.api;

import com.art.anomaly.detector.query.board.api.model.RoomAnomaliesResponse;
import com.art.anomaly.detector.query.board.api.model.ThermometersAnomalyStatisticResponse;
import org.springframework.web.bind.annotation.GetMapping;

public interface TemperatureDetectorApi {


    @GetMapping("/thermometers")
    ThermometersAnomalyStatisticResponse getThermometerAnomalies();

    @GetMapping("/rooms")
    RoomAnomaliesResponse getRoomsAnomalies();


}
