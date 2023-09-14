package com.oluwaseun.dronedispatch.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

}
