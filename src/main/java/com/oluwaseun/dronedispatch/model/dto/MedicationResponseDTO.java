package com.oluwaseun.dronedispatch.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicationResponseDTO {
    private String name;
    private Double weight;
    private String code;
    private String image;
}
