package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.MedicationRequest;
import com.oluwaseun.dronedispatch.model.dto.MedicationResponse;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import org.springframework.data.domain.Page;

public interface MedicationService {
    Medication createMedication(MedicationRequest medicationRequest);
    Page<MedicationResponse> getAllMedication(Integer pageSize, Integer pageIndex);
}
