package com.library.librarysystem.service;

import com.library.librarysystem.model.User;
import com.library.librarysystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUser_success() {
        User newUser = new User(null, "newuser", "plainpass", "email@mail.com", "First", "Last", "1234567890", "ROLE_MEMBER");
        User savedUser = new User(1L, "newuser", "encodedpass", "email@mail.com", "First", "Last", "1234567890", "ROLE_MEMBER");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("plainpass")).thenReturn("encodedpass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(newUser);

        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals("encodedpass", result.getPassword());
    }

    @Test
    void testRegisterUser_duplicateUsername() {
        when(userRepository.existsByUsername("duplicate")).thenReturn(true);

        User duplicateUser = new User(null, "duplicate", "pass", "dup@mail.com", "First", "Last", "1234567890", "ROLE_MEMBER");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(duplicateUser));
    }
}
