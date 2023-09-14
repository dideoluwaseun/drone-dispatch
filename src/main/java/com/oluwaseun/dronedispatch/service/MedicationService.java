package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.MedicationRequestDTO;
import com.oluwaseun.dronedispatch.model.dto.MedicationResponseDTO;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import org.springframework.data.domain.Page;

public interface MedicationService {
    Medication createMedication(MedicationRequestDTO medicationRequestDTO);
    Page<MedicationResponseDTO> getAllMedication(Integer pageSize, Integer pageIndex);
}
