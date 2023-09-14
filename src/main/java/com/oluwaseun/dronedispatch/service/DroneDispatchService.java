package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.entity.Drone;

public interface DroneDispatchService {
    Drone registerDrone(DroneDTO droneDTO);
}
