package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.exception.EntityNotFoundException;
import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.dto.DroneResponse;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.model.entity.DroneState;
import com.oluwaseun.dronedispatch.repository.DroneRepository;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public Page<DroneResponse> getAllDrones(Integer pageIndex, Integer pageSize) {
        log.info("processing get all drones request");
        Page<Drone> dronePage = droneRepository.findAll(PageRequest.of(pageIndex, pageSize));

        log.info("done processing get all drones request");
        return dronePage.map(drone -> DroneResponse.builder()
                .id(drone.getId())
                .state(drone.getState())
                .serialNumber(drone.getSerialNumber())
                .model(drone.getModel())
                .batteryCapacity(drone.getBatteryCapacity())
                .build());
    }

    @Override
    public DroneResponse getDroneById(Long id) {
        log.info("processing get drone by id {} request", id);

        Optional<Drone> drone = droneRepository.findById(id);
        if(drone.isEmpty()) {
            log.error("drone does not exist");
            throw new EntityNotFoundException("drone does not exist");
        }

        log.info("done processing get drone by id {} request", id);

        return DroneResponse.builder()
                .id(drone.get().getId())
                .serialNumber(drone.get().getSerialNumber())
                .state(drone.get().getState())
                .model(drone.get().getModel())
                .batteryCapacity(drone.get().getBatteryCapacity())
                .medications(drone.get().getMedications())
                .build();
    }
}
