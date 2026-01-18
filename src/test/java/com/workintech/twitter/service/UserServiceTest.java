package com.workintech.twitter.service;

import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("Test User");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setUsername("testuser");
    }

    @Test
    void register_ShouldCreateUser_WhenEmailAndUsernameAreUnique() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.register(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.register(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findByEmail(user.getEmail());

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }
}
