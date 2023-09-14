package com.oluwaseun.dronedispatch.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oluwaseun.dronedispatch.model.entity.DroneModel;
import com.oluwaseun.dronedispatch.model.entity.DroneState;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DroneResponse {
    private Long id;
    private String serialNumber;
    private DroneModel model;
    private Integer batteryCapacity;
    private DroneState state;
    private Set<Medication> medications;
}
