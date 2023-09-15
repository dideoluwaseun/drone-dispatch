package com.oluwaseun.dronedispatch.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "name can only contain letters, numbers, underscores and hyphens")
    private String name;

    @Min(value = 0, message = "weight value must be greater than or equal to 0")
    private Double weight;

    @Pattern(regexp = "^[A-Z0-9_]*$", message = "code can contain only capital letters, underscores and numbers")
    private String code;
    private String image;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "medications")
    @JsonBackReference
    private Set<Drone> drones = new HashSet<>();

}
