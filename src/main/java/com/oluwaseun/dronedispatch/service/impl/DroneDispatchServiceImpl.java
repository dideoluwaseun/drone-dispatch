package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DroneWeightLimitExceededException;
import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.exception.EntityNotFoundException;
import com.oluwaseun.dronedispatch.exception.ValidationException;
import com.oluwaseun.dronedispatch.model.dto.DroneRequest;
import com.oluwaseun.dronedispatch.model.dto.DroneResponse;
import com.oluwaseun.dronedispatch.model.dto.UpdateDroneRequest;
import com.oluwaseun.dronedispatch.model.entity.*;
import com.oluwaseun.dronedispatch.repository.AuditEventLogRepository;
import com.oluwaseun.dronedispatch.repository.DroneRepository;
import com.oluwaseun.dronedispatch.repository.MedicationRepository;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
import com.oluwaseun.dronedispatch.model.dto.LoadDroneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneDispatchServiceImpl implements DroneDispatchService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final AuditEventLogRepository auditEventLogRepository;
    private static final double DRONE_MAX_WEIGHT = 500.00;

    @Override
    public Drone registerDrone(DroneRequest droneRequest) {
        log.info("processing register drone request");

        DroneModel droneModel;
        String model = droneRequest.getModel();
            try {
                droneModel = Enum.valueOf(DroneModel.class, model);
            }
            catch (IllegalArgumentException e) {
                log.error("string {} does not match any enum constant for drone model, valid values are LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT", model);
                throw new ValidationException("string "+model+" is not valid drone model, valid values are LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT");
            }

        Drone drone = Drone.builder()
                .serialNumber(droneRequest.getSerialNumber())
                .model(droneModel)
                .batteryCapacity(droneRequest.getBatteryCapacity())
                .state(DroneState.IDLE).build();

        try {
            droneRepository.save(drone);
        } catch (DataIntegrityViolationException e) {
            log.error("drone already exists");
            throw new DuplicateEntityException("drone already exists");
        }

        log.info("done processing register drone request");
        return drone;
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

        if (drone.getBatteryCapacity() < 25) {
            log.error("drone battery level is below 25% and cannot be loaded");
            throw new ValidationException("drone battery level is below 25% and cannot be loaded");
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

    @Override
    public Page<DroneResponse> getAllAvailableDrones(Integer pageIndex, Integer pageSize) {
        log.info("processing get all available drones for loading request");
        DroneState state = DroneState.IDLE;
        Integer batteryCapacity = 25;
        Page<Drone> dronePage = droneRepository.findByStateEqualsAndBatteryCapacityGreaterThanEqual(state, batteryCapacity, PageRequest.of(pageIndex, pageSize));

        log.info("done processing get all available drones for loading request");
        return dronePage.map(drone -> DroneResponse.builder()
                .id(drone.getId())
                .state(drone.getState())
                .serialNumber(drone.getSerialNumber())
                .model(drone.getModel())
                .batteryCapacity(drone.getBatteryCapacity())
                .build());
    }

    @Scheduled(fixedRate = 900000)//every 15 mins
    public void checkAndLogBatteryLevels(){
        int pageSize = 10;
        int pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Drone> dronePage;
        do {
            dronePage = droneRepository.findAll(pageable);

            List<AuditEventLog> auditEventLogs = dronePage.stream().map(
                    drone -> AuditEventLog.builder().drone(drone).time(Instant.now()).batteryCapacity(drone.getBatteryCapacity()).build()).collect(Collectors.toList());
            auditEventLogRepository.saveAll(auditEventLogs);

            log.info("logging audit for battery level check");
            pageNumber++;
            pageable = PageRequest.of(pageNumber, pageSize);
        } while (dronePage.hasNext());
    }

    @Override
    public void updateDroneStateAndBatteryCapacity(UpdateDroneRequest request, Long id) {
        log.info("processing update drone request for {}", id);

        Drone drone = droneRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("drone with id "+id+" does not exist"));

        Integer batteryCapacity = request.getBatteryCapacity();
        if(batteryCapacity !=null) {
            drone.setBatteryCapacity(batteryCapacity);
        }

        DroneState droneState;
        String state = request.getState();
        if(StringUtils.isNotBlank(state)) {
            try {
                droneState = Enum.valueOf(DroneState.class, state);
            }
            catch (IllegalArgumentException e) {
                log.error("string {} does not match any enum constant for drone state, valid values are RETURNING, DELIVERED, LOADING, DELIVERING, IDLE", state);
                throw new ValidationException("string "+state+" is not valid drone state, valid values are RETURNING, DELIVERED, LOADING, DELIVERING, IDLE");
        }
            switch (droneState) {
                case IDLE -> {
                    log.info("setting drone state to idle");
                    drone.setState(DroneState.IDLE);
                }
                case LOADING -> {
                    Integer requestBatteryCapacity = request.getBatteryCapacity();
                    if ((requestBatteryCapacity == null || requestBatteryCapacity >= 25) && drone.getBatteryCapacity() >= 25) {
                        log.info("Setting drone state to loading");
                        drone.setState(DroneState.LOADING);
                    } else {
                        log.error("Drone battery level is below 25% and cannot be loaded");
                        throw new ValidationException("Drone battery level is below 25% and cannot be loaded");
                    }
                }
                case DELIVERING -> {
                    log.info("setting drone state to delivering");
                    drone.setState(DroneState.DELIVERING);
                }
                case DELIVERED -> {
                    log.info("setting drone state to delivered");
                    drone.setState(DroneState.DELIVERED);
                }
                case RETURNING -> {
                    log.info("setting drone state to returning");
                    drone.setState(DroneState.RETURNING);
                }
                default -> {
                    log.warn("Unknown state encountered for update drone: {}", droneState);
                    throw new ValidationException("unknown state " + droneState);
                }
            }
        }

        droneRepository.save(drone);
        log.info("done processing update drone request for {}", id);
    }
 }
