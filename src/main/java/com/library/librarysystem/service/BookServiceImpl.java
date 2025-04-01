package com.library.librarysystem.service;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired 
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Adds a new Book to the system
     * @param book the Book to add
     * @return the saved Book entity
     */
    @Override
    public Book addBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN already exists");
        }
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    /**
     * Deletes a Book by its ID
     * @param id the ID of the Book to delete
     */
    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Updates an existing Book by its ID
     * @param id the ID of the Book to update
     * @param updatedBook the new Book data
     * @return the updated Book entity
     */
    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (!existing.getIsbn().equals(updatedBook.getIsbn())
                && bookRepository.existsByIsbn(updatedBook.getIsbn())) {
            throw new IllegalArgumentException("Another book with this ISBN already exists");
        }
        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setPublicationYear(updatedBook.getPublicationYear());
        return bookRepository.save(existing);
    }

    /**
     * Retrieves all Books in the system
     * @return list of all Books
     */
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Searches Books by title or author
     * @param keyword the search keyword
     * @return list of matching Books
     */
    @Override
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    /**
     * Retrieves a Book by its ID.
     * @param id the ID of the Book
     * @return the found Book
     */
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }
}
