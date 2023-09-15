package com.oluwaseun.dronedispatch.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class LoadDroneRequest {
    @NotNull(message = "drone id cannot be null")
    private Long droneId;

    @NotEmpty(message = "at least one med code is required")
    private Set<String> medicationCodes = new HashSet<>();
}
