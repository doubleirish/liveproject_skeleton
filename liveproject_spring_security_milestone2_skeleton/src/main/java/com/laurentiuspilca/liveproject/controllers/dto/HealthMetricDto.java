package com.laurentiuspilca.liveproject.controllers.dto;


import com.laurentiuspilca.liveproject.entities.HealthMetric;
import com.laurentiuspilca.liveproject.entities.enums.HealthMetricType;
import lombok.Data;

import java.util.List;

@Data
public class HealthMetricDto {
    private HealthMetricType type;
    private double value;

    public HealthMetricDto(HealthMetricType type, double value) {
        this.type = type;
        this.value = value;
    }

    public HealthMetricDto() {
    }
}

