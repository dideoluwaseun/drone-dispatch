package com.oluwaseun.dronedispatch.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MedicationRequest {
    @NotBlank(message = "name is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "name can only contain letters, numbers, underscores and hyphens")
    private String name;

    @NotNull(message = "weight cannot be null")
    @Min(value = 0, message = "Value must be greater than or equal to 0")
    private Double weight;

    @Pattern(regexp = "^[A-Z0-9_]*$", message = "code can contain only capital letters, underscores and numbers")
    @NotBlank(message = "code is required")
    private String code;

    @NotBlank(message = "image path is required")
    private String image;
}
