package com.oluwaseun.dronedispatch.controller;

import com.oluwaseun.dronedispatch.model.dto.SignInResponse;
import com.oluwaseun.dronedispatch.model.dto.SignUpRequest;
import com.oluwaseun.dronedispatch.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestParam String username, @RequestParam String password) {
        SignInResponse userAuthResponse = userService.signIn(username, password);
        return new ResponseEntity<>(userAuthResponse, HttpStatus.OK);
    }

    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.signup(signUpRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
