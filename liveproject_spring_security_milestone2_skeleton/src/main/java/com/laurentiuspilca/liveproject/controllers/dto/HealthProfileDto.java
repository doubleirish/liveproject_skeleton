package com.laurentiuspilca.liveproject.controllers.dto;


import com.laurentiuspilca.liveproject.entities.HealthMetric;
import lombok.Data;

import java.util.List;

@Data
public class HealthProfileDto {
    private String username;
    private List<HealthMetricDto> metrics;

    public HealthProfileDto() {
    }

    public HealthProfileDto(String username, List<HealthMetricDto> metrics) {
        this.username = username;
        this.metrics = metrics;
    }
}

