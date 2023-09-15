package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DroneWeightLimitExceededException;
import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.exception.EntityNotFoundException;
import com.oluwaseun.dronedispatch.exception.ValidationException;
import com.oluwaseun.dronedispatch.model.dto.DroneRequest;
import com.oluwaseun.dronedispatch.model.dto.LoadDroneRequest;
import com.oluwaseun.dronedispatch.model.dto.UpdateDroneRequest;
import com.oluwaseun.dronedispatch.model.entity.Drone;
import com.oluwaseun.dronedispatch.model.entity.DroneState;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.repository.AuditEventLogRepository;
import com.oluwaseun.dronedispatch.repository.DroneRepository;
import com.oluwaseun.dronedispatch.repository.MedicationRepository;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DroneDispatchServiceImplTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private AuditEventLogRepository auditEventLogRepository;

    @InjectMocks
    private DroneDispatchServiceImpl droneDispatchService;

    @Nested
    class RegisterDrone {
        private DroneRequest droneRequest;

        @BeforeEach
        void initObject() {
            //given
            droneRequest = DroneRequest.builder()
                    .serialNumber("DRONE001")
                    .batteryCapacity(50)
                    .model("HEAVYWEIGHT").build();
        }

        @Test
        void registerDrone() {
            //when
            when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());
            //then
            assertThatCode(() -> droneDispatchService.registerDrone(droneRequest)).doesNotThrowAnyException();
        }

        @Test
        void registerDroneThrowDuplicateExceptionWhenDroneExists() {
            //when
            when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(Drone.builder().build()));
            //then
            assertThrows(DuplicateEntityException.class, () -> droneDispatchService.registerDrone(droneRequest));
        }

        @Test
        void registerDroneThrowValidationExceptionWhenInvalidModel() {
            //given
            droneRequest.setModel("light");
            //when
            when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());
            //then
            assertThrows(ValidationException.class, () -> droneDispatchService.registerDrone(droneRequest));
        }
    }


    @Test
    void getAllDrones() {
        //when
        when(droneRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(Drone.builder().build())));
        //then
        assertThatCode(() -> droneDispatchService.getAllDrones(0, 1)).doesNotThrowAnyException();
    }

    @Test
    void getDroneById() {
        //when
        when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().build()));
        //then
        assertThatCode(() -> droneDispatchService.getDroneById(1L)).doesNotThrowAnyException();

    }

    @Test
    void getDroneByIdDroneDoesNotExist() {
        //when
        when(droneRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> droneDispatchService.getDroneById(1L));

    }

    @Nested
    class LoadDrone {
        private LoadDroneRequest request;

        @BeforeEach
        void initObjects(){
            //given
            request = LoadDroneRequest.builder()
                    .droneId(1L)
                    .medicationCodes(Set.of("IBU45", "MET15"))
                    .build();
        }
        @Test
        void loadDrone() {
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().medications(
                    Sets.newHashSet()).state(DroneState.IDLE).batteryCapacity(50).build()));
            when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(Medication.builder().weight(20.0).build()));
            //then
            assertThatCode(()-> droneDispatchService.loadDrone(request)).doesNotThrowAnyException();
        }

        @Test
        void loadDroneThrowValidationExceptionWhenDroneStateIsNotIdle() {
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().medications(
                    Sets.newHashSet()).state(DroneState.DELIVERING).batteryCapacity(50).build()));
            //then
            assertThrows(ValidationException.class, ()-> droneDispatchService.loadDrone(request));
        }

        @Test
        void loadDroneThrowValidationExceptionWhenBatteryCapacityIsLessThan25() {
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().medications(
                    Sets.newHashSet()).state(DroneState.IDLE).batteryCapacity(20).build()));
            //then
            assertThrows(ValidationException.class, ()-> droneDispatchService.loadDrone(request));
        }

        @Test
        void loadDroneThrowDroneWeightLimitExceededExceptionWhenMedWeightExceedsLimit() {
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().medications(
                    Sets.newHashSet()).state(DroneState.IDLE).batteryCapacity(50).build()));
            when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(Medication.builder().weight(550.0).build()));
            //then
            assertThrows(DroneWeightLimitExceededException.class, ()-> droneDispatchService.loadDrone(request));
        }
    }

    @Test
    void getAllAvailableDrones() {
        //when
        when(droneRepository.findByStateEqualsAndBatteryCapacityGreaterThanEqual(any(DroneState.class), anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(Drone.builder().build())));
        //then
        assertThatCode(()-> droneDispatchService.getAllAvailableDrones(0,1)).doesNotThrowAnyException();
    }

    @Test
    void checkAndLogBatteryLevels() {
        //given
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Drone> drones = new ArrayList<>();
        drones.add(Drone.builder().build());

        //when
        when(droneRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(drones));
        droneDispatchService.checkAndLogBatteryLevels();

        //then
        verify(droneRepository, times(1)).findAll(pageable);
        verify(auditEventLogRepository, times(1)).saveAll(anyList());

    }

    @Nested
    class UpdateDrone {
        private UpdateDroneRequest updateDroneRequest;

        @BeforeEach
        void initObject() {
            //given
            updateDroneRequest = UpdateDroneRequest.builder()
                    .batteryCapacity(50)
                    .state("IDLE").build();
        }

        @Test
        void updateDroneStateIdleAndBatteryCapacity() {
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.RETURNING).build()));
            //then
            assertThatCode(() -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L)).doesNotThrowAnyException();
        }

        @Test
        void updateDroneStateInvalidState() {
            //given
            updateDroneRequest.setState("BIRD-IS-IN-THE-AIR");
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.RETURNING).build()));
            //then
            assertThrows(ValidationException.class, () -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L));
        }

        @Test
        void updateDroneStateUnknownState() {
            //given
            updateDroneRequest.setState("LOADED");
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.RETURNING).build()));
            //then
            assertThrows(ValidationException.class, () -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L));
        }

        @Test
        void updateDroneStateLoadingAndBatteryCapacity() {
            //given
            updateDroneRequest.setState("LOADING");
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.IDLE).build()));
            //then
            assertThatCode(() -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L)).doesNotThrowAnyException();
        }

        @Test
        void updateDroneStateLoadingThrowExceptionIfBatteryLessThan25() {
            //given
            updateDroneRequest.setState("LOADING");
            updateDroneRequest.setBatteryCapacity(null);
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(20).state(DroneState.IDLE).build()));
            //then
            assertThrows(ValidationException.class, () -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L));
        }

        @Test
        void updateDroneStateDeliveringAndBatteryCapacity() {
            //given
            updateDroneRequest.setState("DELIVERING");
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.LOADED).build()));
            //then
            assertThatCode(() -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L)).doesNotThrowAnyException();
        }

        @Test
        void updateDroneStateDeliveredAndBatteryCapacity() {
            //given
            updateDroneRequest.setState("DELIVERED");
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.DELIVERING).build()));
            //then
            assertThatCode(() -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L)).doesNotThrowAnyException();
        }

        @Test
        void updateDroneStateReturningAndBatteryCapacity() {
            //given
            updateDroneRequest.setState("RETURNING");
            //when
            when(droneRepository.findById(anyLong())).thenReturn(Optional.of(Drone.builder().batteryCapacity(30).state(DroneState.DELIVERED).build()));
            //then
            assertThatCode(() -> droneDispatchService.updateDroneStateAndBatteryCapacity(updateDroneRequest, 1L)).doesNotThrowAnyException();
        }
    }
}