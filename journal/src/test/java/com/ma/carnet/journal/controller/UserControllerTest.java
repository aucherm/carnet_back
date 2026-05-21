package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    @Test
    void findAll_shouldReturnAllUsers() {
        // GIVEN
        User user1 = new User();
        user1.setMail("marie@test.com");

        User user2 = new User();
        user2.setMail("jean@test.com");

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        // WHEN
        ResponseEntity<List<User>> response = userController.findAll();

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void findById_shouldReturn200_whenUserExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setIdUser(id);
        user.setMail("marie@test.com");

        when(userService.findById(id)).thenReturn(Optional.of(user));

        // WHEN
        ResponseEntity<User> response = userController.findById(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMail()).isEqualTo("marie@test.com");
    }

    @Test
    void findById_shouldReturn404_whenUserNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(userService.findById(id)).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<User> response = userController.findById(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void update_shouldReturn200_whenUserExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setIdUser(id);
        user.setMail("marie@test.com");
        user.setRole("USER");

        when(userService.findById(id)).thenReturn(Optional.of(user));
        when(userService.save(any(User.class))).thenReturn(user);

        // WHEN
        ResponseEntity<User> response = userController.update(id, user);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void update_shouldReturn404_whenUserNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userService.findById(id)).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<User> response = userController.update(id, user);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void delete_shouldReturn204_whenUserExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setIdUser(id);

        when(userService.findById(id)).thenReturn(Optional.of(user));

        // WHEN
        ResponseEntity<Void> response = userController.delete(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(userService, times(1)).delete(id);
    }

    @Test
    void delete_shouldReturn404_whenUserNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(userService.findById(id)).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<Void> response = userController.delete(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void getCurrentUser_shouldReturnUser_whenAuthenticated() {
        // GIVEN
        User user = new User();
        user.setMail("marie@test.com");

        when(authentication.getName()).thenReturn("marie@test.com");
        when(userService.findByMail("marie@test.com")).thenReturn(Optional.of(user));

        // WHEN
        ResponseEntity<User> response = userController.getCurrentUser(authentication);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getMail()).isEqualTo("marie@test.com");
    }
}