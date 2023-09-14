package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.model.dto.MedicationRequest;
import com.oluwaseun.dronedispatch.model.dto.MedicationResponse;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.repository.MedicationRepository;
import com.oluwaseun.dronedispatch.service.MedicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;

    @Override
    public Medication createMedication(MedicationRequest medicationRequest) {
        log.info("processing create medication request");
        Optional<Medication> medication = medicationRepository.findByCode(medicationRequest.getCode());

        if(medication.isPresent()) {
            log.error("medication already exists");
            throw new DuplicateEntityException("medication already exists");
        }

        log.info("done processing create medication request");
        return medicationRepository.save(Medication.builder()
                .name(medicationRequest.getName())
                .code(medicationRequest.getCode())
                .weight(medicationRequest.getWeight())
                .image(medicationRequest.getImage())
                .build());
    }

    @Override
    public Page<MedicationResponse> getAllMedication(Integer pageSize, Integer pageIndex) {
        log.info("processing get all medications request");
        Page<Medication> medicationPage = medicationRepository.findAll(PageRequest.of(pageIndex, pageSize));

        log.info("done processing get all medication request");
        return medicationPage.map(medication -> MedicationResponse.builder()
                .name(medication.getName())
                .code(medication.getCode())
                .weight(medication.getWeight())
                .image(medication.getImage())
                .build());
    }
}
