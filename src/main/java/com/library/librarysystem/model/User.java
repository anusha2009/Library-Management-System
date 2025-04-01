package com.library.librarysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique username
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    // Unique email
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phoneNumber;

    @Column(nullable = false)
    private String role; // ROLE_MEMBER or ROLE_LIBRARIAN

    public boolean isLibrarian() {
        return "ROLE_LIBRARIAN".equalsIgnoreCase(this.role);
    }

    public boolean isMember() {
        return "ROLE_MEMBER".equalsIgnoreCase(this.role);
    }
}
