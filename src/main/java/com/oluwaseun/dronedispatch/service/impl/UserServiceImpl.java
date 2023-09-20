package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.exception.UnauthorisedException;
import com.oluwaseun.dronedispatch.model.dto.SignInResponse;
import com.oluwaseun.dronedispatch.model.dto.SignUpRequest;
import com.oluwaseun.dronedispatch.model.entity.AppUser;
import com.oluwaseun.dronedispatch.model.entity.UserRole;
import com.oluwaseun.dronedispatch.repository.UserRepository;
import com.oluwaseun.dronedispatch.security.JwtTokenProvider;
import com.oluwaseun.dronedispatch.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public SignInResponse signIn(String username, String password) {
        log.info("processing sign in request");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserRole userRole = userRepository.findByUsername(username).getUserRole();
            String token = jwtTokenProvider.generateToken(username, userRole);

            log.info("done processing sign in request");

            return SignInResponse.builder().username(username).userRole(userRole).accessToken(token).build();
        } catch (AuthenticationException e) {
            log.info("Incorrect user credentials");
            throw new UnauthorisedException(e.getMessage());
        }
    }

    @Override
    public void signup(SignUpRequest request) {
        log.info("processing sign up request");

        try{
            userRepository.save(AppUser.builder()
                    .username(request.getUsername())
                    .userRole(UserRole.ROLE_USER)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build());
        } catch (DataIntegrityViolationException e) {
            log.error("user already exists");
            throw new DuplicateEntityException("Username is already in use");
        }
        log.info("done processing sign up request");
    }
}
