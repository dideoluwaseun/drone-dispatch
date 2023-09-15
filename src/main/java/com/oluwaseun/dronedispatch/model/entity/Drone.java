package com.oluwaseun.dronedispatch.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^.{0,100}$")
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Min(value = 1, message = "Value must be greater than or equal to 1")
    @Max(value = 100, message = "Value must be less than or equal to 100")
    private Integer batteryCapacity;
    @Enumerated(EnumType.STRING)
    private DroneState state;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "drone_medication",
            joinColumns = @JoinColumn(name = "drone_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    @JsonManagedReference
    private Set<Medication> medications = new HashSet<>();

    @OneToMany
    @JsonBackReference
    private Set<AuditEventLog> auditEventLogs = new HashSet<>();

    public void addMedications(Set<Medication> medications) {
        this.medications.addAll(medications);
    }
}
