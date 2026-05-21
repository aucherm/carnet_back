package com.ma.carnet.journal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ma.carnet.journal.dto.LoginRequest;
import com.ma.carnet.journal.dto.SignupRequest;
import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.service.JwtService;
import com.ma.carnet.journal.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    // =========================
    // signup
    // =========================

    @Test
    void signup_shouldReturn200_whenValidRequest() {
        // GIVEN
        SignupRequest request = new SignupRequest();
        request.setFirstName("Marie");
        request.setLastName("Dupont");
        request.setMail("marie@test.com");
        request.setPassword("motdepasse");

        User savedUser = new User();
        savedUser.setIdUser(UUID.randomUUID());
        savedUser.setFirstName("Marie");
        savedUser.setLastName("Dupont");
        savedUser.setMail("marie@test.com");
        savedUser.setPassword("hashed");
        savedUser.setRole("USER");

        when(passwordEncoder.encode("motdepasse")).thenReturn("hashed");
        when(userService.save(any(User.class))).thenReturn(savedUser);

        // WHEN
        ResponseEntity<User> response = authController.signup(request);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMail()).isEqualTo("marie@test.com");
    }

    // =========================
    // login
    // =========================

    @Test
    void login_shouldReturnToken_whenValidCredentials() {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setMail("marie@test.com");
        request.setPassword("motdepasse");

        User user = new User();
        user.setIdUser(UUID.randomUUID());
        user.setMail("marie@test.com");
        user.setRole("USER");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.findByMail("marie@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(86400000L);

        // WHEN
        var response = authController.login(request);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo("fake-jwt-token");
    }

    @Test
    void login_shouldThrowException_whenBadCredentials() {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setMail("marie@test.com");
        request.setPassword("mauvaismdp");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Identifiants incorrects"));

        // WHEN & THEN
        assertThrows(BadCredentialsException.class, () ->
                authController.login(request)
        );
    }
}