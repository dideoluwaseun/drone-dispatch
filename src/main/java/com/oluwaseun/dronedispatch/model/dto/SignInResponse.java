package com.oluwaseun.dronedispatch.model.dto;

import com.oluwaseun.dronedispatch.model.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String username;
    private UserRole userRole;
    private String accessToken;
}
