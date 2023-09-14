package com.oluwaseun.dronedispatch.controller;

import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.dto.MedicationDTO;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
import com.oluwaseun.dronedispatch.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drone-dispatch")
public class DispatchController {
    private final DroneDispatchService droneDispatchService;
    private final MedicationService medicationService;

    @PostMapping("/drone/register")
    public ResponseEntity<Drone> registerDrone(@RequestBody @Valid DroneDTO droneDTO) {
        return new ResponseEntity<>(droneDispatchService.registerDrone(droneDTO), HttpStatus.CREATED);
    }

    @PostMapping("/medication")
    public ResponseEntity<Medication> createMedication(@RequestBody @Valid MedicationDTO medicationDTO) {
        return new ResponseEntity<>(medicationService.createMedication(medicationDTO), HttpStatus.CREATED);
    }

}
