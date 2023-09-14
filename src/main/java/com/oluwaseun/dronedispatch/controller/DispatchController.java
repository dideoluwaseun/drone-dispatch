package com.oluwaseun.dronedispatch.controller;

import com.oluwaseun.dronedispatch.model.dto.DroneDTO;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.service.DroneDispatchService;
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

    @PostMapping("/drone/register")
    public ResponseEntity<Drone> registerDrone(@RequestBody @Valid DroneDTO droneDTO) {
        return new ResponseEntity<>(droneDispatchService.registerDrone(droneDTO), HttpStatus.OK);
    }

}
