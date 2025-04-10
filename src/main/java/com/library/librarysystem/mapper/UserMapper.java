package com.library.librarysystem.mapper;

import com.library.librarysystem.dto.UserDTO;
import com.library.librarysystem.model.User;

public class UserMapper {

    public static UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
