package com.art.anomaly.detector.query.board.api.model;

import java.util.List;
import java.util.UUID;

public class RoomAnomaliesResponse {
    private UUID roomId;

    private List<AnomalyDto> anomalies;
}
