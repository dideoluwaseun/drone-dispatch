package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DroneWeightLimitExceededException;
import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.exception.EntityNotFoundException;
import com.oluwaseun.dronedispatch.exception.ValidationException;
import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.dto.DroneResponse;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.model.entity.DroneState;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.repository.DroneRepository;
import com.oluwaseun.dronedispatch.repository.MedicationRepository;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
import com.oluwaseun.dronedispatch.model.dto.LoadDroneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneDispatchServiceImpl implements DroneDispatchService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    private static final double DRONE_MAX_WEIGHT = 500.00;

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

    @Override
    public void loadDrone(LoadDroneRequest request) {
        log.info("processing load drone id {} request", request.getDroneId());
        Drone drone = droneRepository.findById(request.getDroneId()).orElseThrow(() -> new EntityNotFoundException("drone does not exist"));

        if (!drone.getState().equals(DroneState.IDLE)) {
            log.error("drone is being loaded or currently in use and cannot be loaded");
            throw new ValidationException("drone is being loaded or currently in use and cannot be loaded");
        }

        Set<Medication> medications = request.getMedicationCodes().stream().map(
                code -> medicationRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("medication "+code+" does not exist"))).collect(Collectors.toSet());

        checkWeightLimit(medications);
        drone.addMedications(medications);
        log.info("loading drone");

        drone.setState(DroneState.LOADED);
        droneRepository.save(drone);
        log.info("done processing load drone id {} request", request.getDroneId());
    }

    public void checkWeightLimit(Set<Medication> medications) {
        double sumOfWeights = 0.0;

        for (Medication medication : medications) {
            sumOfWeights += medication.getWeight();
            if (sumOfWeights > DRONE_MAX_WEIGHT) {
                log.error("sum of medication weights exceeds the limit");
                throw new DroneWeightLimitExceededException("sum of medication weights exceeds the limit");
            }
        }
    }
}
