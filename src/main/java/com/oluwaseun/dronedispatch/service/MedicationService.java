package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.MedicationDTO;
import com.oluwaseun.dronedispatch.model.entity.Medication;

public interface MedicationService {
    Medication createMedication(MedicationDTO medicationDTO);
}
