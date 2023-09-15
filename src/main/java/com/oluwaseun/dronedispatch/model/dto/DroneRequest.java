package com.oluwaseun.dronedispatch.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DroneRequest {
    @NotBlank(message = "serial number is required")
    @Pattern(regexp = "^.{0,100}$", message = "serial number should be 100 characters max")
    private String serialNumber;

    @NotBlank(message = "model is required")
    private String model;

    @NotNull(message = "battery capacity is required")
    @Min(value = 1, message = "battery capacity value must be greater than or equal to 1")
    @Max(value = 100, message = "battery capacity value must be less than or equal to 100")
    private Integer batteryCapacity;
}
