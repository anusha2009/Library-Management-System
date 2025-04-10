package com.library.librarysystem.controller;

import com.library.librarysystem.model.User;
import com.library.librarysystem.service.UserService;
import com.library.librarysystem.dto.UserDTO;
import com.library.librarysystem.mapper.UserMapper;
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
    public ResponseEntity<UserDTO> registerMember(@Valid @RequestBody User user) {
        user.setRole("ROLE_MEMBER");
        User saved = userService.registerUser(user);
        return ResponseEntity.ok(UserMapper.convertToDTO(saved));
    }

    // Register a new Librarian
    @PostMapping("/librarian")
    public ResponseEntity<UserDTO> registerLibrarian(@Valid @RequestBody User user) {
        user.setRole("ROLE_LIBRARIAN");
        User saved = userService.registerUser(user);
        return ResponseEntity.ok(UserMapper.convertToDTO(saved));
    }
}
