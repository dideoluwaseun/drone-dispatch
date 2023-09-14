package com.oluwaseun.dronedispatch.service;

import com.oluwaseun.dronedispatch.model.dto.SignInResponse;
import com.oluwaseun.dronedispatch.model.dto.SignUpRequest;

public interface UserService {
    SignInResponse signIn(String username, String password);

    void signup(SignUpRequest request);

}
