package com.library.librarysystem.repository;

import com.library.librarysystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Find books by Title or Author
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    // Check if a book with the given ISBN exists
    boolean existsByIsbn(String isbn);
}
