package com.library.librarysystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Title of the book
    @Column(nullable = false)
    private String title;

    // Author of the book
    @Column(nullable = false)
    private String author;

    // Unique ISBN to identify each book
    @Column(unique = true, nullable = false)
    private String isbn;

    // Option publication year
    private int publicationYear;

    // True if book is available to borrow (default value is true)
    private boolean available = true;
}
