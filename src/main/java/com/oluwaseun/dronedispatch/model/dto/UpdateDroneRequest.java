package com.oluwaseun.dronedispatch.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateDroneRequest {
    private String state;

    @Min(value = 1, message = "battery capacity value must be greater than or equal to 1")
    @Max(value = 100, message = "battery capacity value must be less than or equal to 100")
    private Integer batteryCapacity;
}
