package com.oluwaseun.dronedispatch.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String name;

    private Double weight;

    @Pattern(regexp = "^[A-Z0-9_]*$")
    private String code;
    private String image;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "medications")
    private Set<Drone> drones = new HashSet<>();

}
