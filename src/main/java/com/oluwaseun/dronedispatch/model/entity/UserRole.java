package com.oluwaseun.dronedispatch.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER;

    public String getAuthority() {
        return name();
    }
}
