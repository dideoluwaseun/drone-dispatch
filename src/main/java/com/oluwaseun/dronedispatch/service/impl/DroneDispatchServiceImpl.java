package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.model.entity.DroneState;
import com.oluwaseun.dronedispatch.repository.DroneRepository;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneDispatchServiceImpl implements DroneDispatchService {
    private final DroneRepository droneRepository;

    @Override
    public Drone registerDrone(DroneDTO droneDTO) {
        log.info("processing register drone request");
        Optional<Drone> drone = droneRepository.findBySerialNumber(droneDTO.getSerialNumber());

        if(drone.isPresent()) {
            log.error("drone already exists");
            throw new DuplicateEntityException("drone already exists");
        }

        log.info("done processing register drone request");
        return droneRepository.save(Drone.builder()
                .serialNumber(droneDTO.getSerialNumber())
                .model(droneDTO.getModel())
                .batteryCapacity(droneDTO.getBatteryCapacity())
                .state(DroneState.IDLE).build());
    }
}
