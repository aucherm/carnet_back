package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByMail_shouldReturnUser_whenExists() {
        // GIVEN
        User user = new User();
        user.setMail("test@test.com");
        user.setFirstName("Marie");
        user.setLastName("Dupont");
        user.setPassword("hashed");
        user.setRole("USER");

        when(userRepository.findByMail("test@test.com")).thenReturn(Optional.of(user));

        // WHEN
        Optional<User> result = userService.findByMail("test@test.com");

        // THEN
        assertThat(result).isPresent();
        assertThat(result.get().getMail()).isEqualTo("test@test.com");
    }

    @Test
    void findByMail_shouldReturnEmpty_whenNotExists() {
        // GIVEN
        when(userRepository.findByMail("inconnu@test.com")).thenReturn(Optional.empty());

        // WHEN
        Optional<User> result = userService.findByMail("inconnu@test.com");

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setIdUser(id);
        user.setMail("test@test.com");
        user.setRole("USER");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // WHEN
        Optional<User> result = userService.findById(id);

        // THEN
        assertThat(result).isPresent();
        assertThat(result.get().getIdUser()).isEqualTo(id);
    }
}