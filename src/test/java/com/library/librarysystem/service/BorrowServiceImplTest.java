package com.library.librarysystem.service;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BorrowRecord;
import com.library.librarysystem.model.User;
import com.library.librarysystem.repository.BookRepository;
import com.library.librarysystem.repository.BorrowRecordRepository;
import com.library.librarysystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceImplTest {

    private BorrowRecordRepository borrowRecordRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private BorrowServiceImpl borrowService;

    @BeforeEach
    void setUp() {
        borrowRecordRepository = mock(BorrowRecordRepository.class);
        bookRepository = mock(BookRepository.class);
        userRepository = mock(UserRepository.class);
        borrowService = new BorrowServiceImpl(borrowRecordRepository, bookRepository, userRepository);
    }

    @Test
    void testBorrowBook() {
        Book book = new Book(1L, "Test Book", "Author", "123", 2020, true);
        User member = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_MEMBER");
        User librarian = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_LIBRARIAN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(member));
        when(userRepository.findById(2L)).thenReturn(Optional.of(librarian));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BorrowRecord result = borrowService.borrowBook(1L, 1L, 2L);

        assertEquals(member, result.getMember());
        assertEquals(librarian, result.getLibrarian());
        assertFalse(result.isReturned());
        assertFalse(book.isAvailable());
    }

    @Test
    void testReturnBook() {
        Book book = new Book(1L, "Book", "Author", "123", 2020, false);
        User member = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_MEMBER");
        User librarian = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_LIBRARIAN");

        BorrowRecord record = new BorrowRecord(false, 1L, member, librarian, book, LocalDate.now(), null, BorrowRecord.Status.BORROWED);

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(borrowRecordRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BorrowRecord result = borrowService.returnBook(1L);

        assertTrue(result.isReturned());
        assertTrue(result.getBook().isAvailable());
        assertNotNull(result.getReturnDate());
    }


    @Test
    void testBorrowBookException() {
        Book book = new Book(1L, "Book", "Author", "123", 2020, false);
        User member = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_MEMBER");
        User librarian = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_LIBRARIAN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(member));
        when(userRepository.findById(2L)).thenReturn(Optional.of(librarian));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(IllegalStateException.class, () -> borrowService.borrowBook(1L, 1L, 2L));
    }

    @Test
    void testReturnBookException() {
        Book book = new Book(1L, "Book", "Author", "123", 2020, false);
        User member = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_MEMBER");
        User librarian = new User(null, "bob", "securepass", "Bob", "Brown", "bob@mail.com", "1234567891", "ROLE_LIBRARIAN");

        BorrowRecord record = new BorrowRecord(true, 1L, member, librarian, book, LocalDate.now().minusDays(2), LocalDate.now(), BorrowRecord.Status.RETURNED);
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        assertThrows(IllegalStateException.class, () -> borrowService.returnBook(1L));
    }

    @Test
    void testGetBorrowHistoryByBook() {
        Book book = new Book(1L, "Test Book", "Author", "123", 2020, true);
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.findByBook(book)).thenReturn(List.of(record));

        List<BorrowRecord> results = borrowService.getBorrowHistoryByBook(1L);

        assertEquals(1, results.size());
        assertEquals(book, results.get(0).getBook());
    }

    @Test
    void testGetBorrowHistoryByMember() {
        User member = new User(null, "alice", "securepass", "Alice", "Smith", "alice@mail.com", "1234567890", "ROLE_MEMBER");
        BorrowRecord record = new BorrowRecord();
        record.setMember(member);

        when(userRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.findByMember(member)).thenReturn(List.of(record));

        List<BorrowRecord> results = borrowService.getBorrowHistoryByMember(1L);

        assertEquals(1, results.size());
        assertEquals(member, results.get(0).getMember());
    }
}
