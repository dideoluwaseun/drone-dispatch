package com.oluwaseun.dronedispatch.controller;

import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.dto.DroneResponse;
import com.oluwaseun.dronedispatch.model.dto.MedicationRequestDTO;
import com.oluwaseun.dronedispatch.model.dto.MedicationResponseDTO;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
import com.oluwaseun.dronedispatch.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Medication> createMedication(@RequestBody @Valid MedicationRequestDTO medicationRequestDTO) {
        return new ResponseEntity<>(medicationService.createMedication(medicationRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/medication")
    public ResponseEntity<Page<MedicationResponseDTO>> getAllMedications(@RequestParam Integer pageSize, Integer pageIndex) {
        return new ResponseEntity<>(medicationService.getAllMedication(pageSize, pageIndex), HttpStatus.OK);
    }

    @GetMapping("/drone")
    public ResponseEntity<Page<DroneResponse>> getAllDrones(@RequestParam Integer pageIndex, Integer pageSize) {
        return new ResponseEntity<>(droneDispatchService.getAllDrones(pageIndex, pageSize), HttpStatus.OK);
    }
}
