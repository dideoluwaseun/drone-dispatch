package com.oluwaseun.dronedispatch.service.impl;

import com.oluwaseun.dronedispatch.model.entity.AppUser;
import com.oluwaseun.dronedispatch.model.entity.UserRole;
import com.oluwaseun.dronedispatch.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    class LoadUserByUserName {
        @Test
        void loadUserByUsername() {
            //when
            when(userRepository.findByUsername(anyString())).thenReturn(AppUser.builder()
                    .username("elonMusk").userRole(UserRole.ROLE_USER).password("encodedPassword").build());

            //then
            assertThatCode(() -> userDetailsService.loadUserByUsername("elonMusk")).doesNotThrowAnyException();
        }

        @Test
        void throwsException() {
            //when
            when(userRepository.findByUsername(anyString())).thenReturn(null);

            //then
            assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("elonMusk"));
        }
    }
}