package com.oluwaseun.dronedispatch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^.{0,100}$")
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private DroneModel model;
    private Integer batteryCapacity;
    @Enumerated(EnumType.STRING)
    private DroneState state;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "drones_medications",
            joinColumns = @JoinColumn(name = "drones_id"),
            inverseJoinColumns = @JoinColumn(name = "medications_id")
    )
    private Set<Medication> medications = new HashSet<>();

    @OneToMany
    private Set<AuditEventLog> auditEventLogs = new HashSet<>();
}
