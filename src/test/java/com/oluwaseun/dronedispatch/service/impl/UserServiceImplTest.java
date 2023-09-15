package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.exception.DuplicateEntityException;
import com.oluwaseun.dronedispatch.exception.UnauthorisedException;
import com.oluwaseun.dronedispatch.model.dto.SignUpRequest;
import com.oluwaseun.dronedispatch.model.entity.AppUser;
import com.oluwaseun.dronedispatch.model.entity.UserRole;
import com.oluwaseun.dronedispatch.repository.UserRepository;
import com.oluwaseun.dronedispatch.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private  AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    class SignIn {
        @Test
        void signIn() {
            //when
            when(userRepository.findByUsername(anyString())).thenReturn(AppUser.builder()
                    .username("user@gmail.com").userRole(UserRole.ROLE_USER).build());

            Authentication authentication = new UsernamePasswordAuthenticationToken("elonMusk", "password");
            when(authenticationManager.authenticate(any())).thenReturn(authentication);

            String token = "asdfghjk45678";
            when(jwtTokenProvider.generateToken(anyString(), any())).thenReturn(token);

            //then
            assertThatCode(() -> userService.signIn("elonMusk", "password")).doesNotThrowAnyException();
        }

        @Test
        void catchException() {
            //when
            when(userRepository.findByUsername(anyString())).thenReturn(AppUser.builder()
                    .username("user@gmail.com").userRole(UserRole.ROLE_USER).build());

            when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("incorrect password"));

            //then
            assertThrows(UnauthorisedException.class, () -> userService.signIn("user@gmail.com", "password"));
        }
    }

    @Nested
    class SignUp {
        SignUpRequest request;

        @BeforeEach
        void initObject() {
            //given
            request = SignUpRequest.builder()
                    .username("elonMusk")
                    .password("password")
                    .build();
        }

        @Test
        void signup() {
            //when
            when(userRepository.existsByUsername(anyString())).thenReturn(false);

            //then
            assertThatCode(() -> userService.signup(request)).doesNotThrowAnyException();
        }

        @Test
        void catchException() {
            //when
            when(userRepository.existsByUsername(anyString())).thenReturn(true);

            //then
            assertThrows(DuplicateEntityException.class, () -> userService.signup(request));
        }
    }
}