package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.dto.DroneResponse;
import com.oluwaseun.dronedispatch.model.dto.LoadDroneRequest;
import com.oluwaseun.dronedispatch.model.dto.UpdateDroneRequest;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import org.springframework.data.domain.Page;

public interface DroneDispatchService {
    Drone registerDrone(DroneDTO droneDTO);
    Page<DroneResponse> getAllDrones(Integer pageIndex, Integer pageSize);
    DroneResponse getDroneById(Long id);
    void loadDrone(LoadDroneRequest request);
    Page<DroneResponse> getAllAvailableDrones(Integer pageIndex, Integer pageSize);
    void updateDroneStateAndBatteryCapacity(UpdateDroneRequest request, Long id);
}
