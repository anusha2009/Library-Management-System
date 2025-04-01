package com.library.librarysystem.controller;

import com.library.librarysystem.model.User;
import com.library.librarysystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new Member
    @PostMapping("/member")
    public ResponseEntity<User> registerMember(@Valid @RequestBody User user) {
        user.setRole("ROLE_MEMBER");
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // Register a new Librarian
    @PostMapping("/librarian")
    public ResponseEntity<User> registerLibrarian(@Valid @RequestBody User user) {
        user.setRole("ROLE_LIBRARIAN");
        return ResponseEntity.ok(userService.registerUser(user));
    }
}
