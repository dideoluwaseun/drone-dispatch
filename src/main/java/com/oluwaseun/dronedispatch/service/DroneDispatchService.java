package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.dto.DroneResponse;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import org.springframework.data.domain.Page;

public interface DroneDispatchService {
    Drone registerDrone(DroneDTO droneDTO);
    Page<DroneResponse> getAllDrones(Integer pageIndex, Integer pageSize);

}
