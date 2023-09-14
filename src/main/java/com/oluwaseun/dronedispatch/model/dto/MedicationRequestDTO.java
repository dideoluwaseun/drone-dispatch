package com.oluwaseun.dronedispatch.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MedicationRequestDTO {
    @NotBlank(message = "name is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "name can only contain letters, numbers, underscores and hyphens")
    private String name;

    @NotNull(message = "weight cannot be null")
    private Double weight;

    @Pattern(regexp = "^[A-Z0-9_]*$", message = "code can contain only capital letters, underscores and numbers")
    @NotBlank
    private String code;

    @NotBlank(message = "image path is required")
    private String image;
}
