package com.oluwaseun.dronedispatch.model.dto;

import com.oluwaseun.dronedispatch.model.entity.DroneModel;
import com.oluwaseun.dronedispatch.model.entity.DroneState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DroneResponse {
    private Long id;
    private String serialNumber;
    private DroneModel model;
    private Integer batteryCapacity;
    private DroneState state;
}
