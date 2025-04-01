package com.library.librarysystem.repository;

import com.library.librarysystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by their username
    Optional<User> findByUsername(String username);
    // Check if a user with the given username exists
    boolean existsByUsername(String username);
    // Check if a user with the given email exists
    boolean existsByEmail(String email);
}
