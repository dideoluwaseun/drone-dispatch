package com.oluwaseun.dronedispatch.model.dto;

import com.oluwaseun.dronedispatch.model.entity.DroneModel;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DroneDTO {
    @NotBlank(message = "serial number is required")
    @Pattern(regexp = "^.{0,100}$", message = "serial number should be 100 characters max")
    private String serialNumber;

    @NotNull(message = "model is required")
    private DroneModel model;

    @NotNull(message = "battery capacity is required")
    @Min(value = 1, message = "Value must be greater than or equal to 1")
    @Max(value = 100, message = "Value must be less than or equal to 100")
    private Integer batteryCapacity;
}
