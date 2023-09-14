package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.model.dto.MedicationDTO;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.repository.MedicationRepository;
import com.oluwaseun.dronedispatch.service.MedicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;

    @Override
    public Medication createMedication(MedicationDTO medicationDTO) {
        log.info("processing create medication request");
        Optional<Medication> medication = medicationRepository.findByCode(medicationDTO.getCode());

        if(medication.isPresent()) {
            log.error("medication already exists");
            throw new DuplicateEntityException("medication already exists");
        }

        log.info("done processing create medication request");
        return medicationRepository.save(Medication.builder()
                .name(medicationDTO.getName())
                .code(medicationDTO.getCode())
                .weight(medicationDTO.getWeight())
                .image(medicationDTO.getImage())
                .build());
    }
}
