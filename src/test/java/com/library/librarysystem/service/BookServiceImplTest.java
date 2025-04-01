package com.library.librarysystem.service;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook() {
        Book book = new Book(null, "Java Tutorial", "John Doe", "15234", 2015, true);

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertTrue(result.isAvailable());
        assertEquals("Java Tutorial", result.getTitle());
    }

    @Test
    void testGetBookById_BookExists() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "12345", 2005, true);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(2L));
    }

    @Test
    void testSearchBooks() {
        List<Book> expected = List.of(new Book(1L, "Python Tutorial", "John Doe", "12543", 2015, true));
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("python", "python"))
                .thenReturn(expected);

        List<Book> result = bookService.searchBooks("python");

        assertEquals(1, result.size());
        assertEquals("Python Tutorial", result.get(0).getTitle());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
