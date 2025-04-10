package com.library.librarysystem.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
