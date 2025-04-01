package com.library.librarysystem.service;

import com.library.librarysystem.model.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    void deleteBook(Long id);
    Book updateBook(Long id, Book updatedBook);
    List<Book> getAllBooks();
    List<Book> searchBooks(String keyword);
    Book getBookById(Long id);
}
