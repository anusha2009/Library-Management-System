package com.library.librarysystem.service;

import com.library.librarysystem.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
    //User getUserById(Long id);
    //List<User> getUsersByRole(String role);
    //boolean existsByEmail(String email);
}
