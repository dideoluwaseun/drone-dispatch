package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.model.dto.MedicationRequest;
import com.oluwaseun.dronedispatch.model.entity.Medication;
import com.oluwaseun.dronedispatch.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MedicationServiceImplTest {
    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationServiceImpl medicationService;

    @Nested
    class CreateMedication {
        private MedicationRequest request;
        @BeforeEach
        void initObject() {
            //given
            request = MedicationRequest.builder()
                    .name("Paracetamol")
                    .code("PARA_25")
                    .weight(25.0)
                    .image("image_url")
                    .build();
        }
        @Test
        void createMedication() {
            //when
            when(medicationRepository.findByCode(anyString())).thenReturn(Optional.empty());
            medicationService.createMedication(request);
            //then
            verify(medicationRepository, times(1)).save(ArgumentMatchers.any(Medication.class));
        }

        @Test
        void createMedicationThrowDuplicateExceptionIfMedAlreadyExists() {
            //when
            when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(Medication.builder().build()));

            //then
            assertThrows(DuplicateEntityException.class, ()->medicationService.createMedication(request));

        }
    }

    @Test
    void getAllMedication() {
        //when
        when(medicationRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(Medication.builder().build())));
        //then
        assertThatCode(() -> medicationService.getAllMedication(1, 0)).doesNotThrowAnyException();
    }
}