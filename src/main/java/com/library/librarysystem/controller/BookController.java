package com.library.librarysystem.controller;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.User;
import com.library.librarysystem.service.BookService;
import com.library.librarysystem.service.UserService;
import com.library.librarysystem.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUtil authUtil;

    // Get all books (accessible by both Member and Librarian)
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Get book by ID (accessible by both Member and Librarian)
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Add a new book (accessible only by Librarian)
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        User user = authUtil.getCurrentUser();
        if (!user.isLibrarian()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(bookService.addBook(book));
    }

    // Update existing book (accessible only by Librarian)
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        User user = authUtil.getCurrentUser();
        if (!user.isLibrarian()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    // Delete a book (accessible only by Librarian)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        User user = authUtil.getCurrentUser();
        if (!user.isLibrarian()) {
            return ResponseEntity.status(403).build();
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // Search for books by title or author (accessible by both Member and Librarian)
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }
} 

