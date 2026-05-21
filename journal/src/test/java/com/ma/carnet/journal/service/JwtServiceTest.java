package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
                86400000L
        );

        user = new User();
        user.setMail("marie@test.com");
        user.setRole("USER");
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        // WHEN
        String token = jwtService.generateToken(user);

        // THEN
        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUsername_shouldReturnMail() {
        // GIVEN
        String token = jwtService.generateToken(user);

        // WHEN
        String mail = jwtService.extractUsername(token);

        // THEN
        assertThat(mail).isEqualTo("marie@test.com");
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValid() {
        // GIVEN
        String token = jwtService.generateToken(user);

        // WHEN
        boolean isValid = jwtService.isTokenValid(token, user);

        // THEN
        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenWrongUser() {
        // GIVEN
        String token = jwtService.generateToken(user);

        User otherUser = new User();
        otherUser.setMail("autre@test.com");
        otherUser.setRole("USER");

        // WHEN
        boolean isValid = jwtService.isTokenValid(token, otherUser);

        // THEN
        assertThat(isValid).isFalse();
    }

    @Test
    void getExpirationTime_shouldReturnConfiguredValue() {
        // WHEN
        long expiration = jwtService.getExpirationTime();

        // THEN
        assertThat(expiration).isEqualTo(86400000L);
    }
}