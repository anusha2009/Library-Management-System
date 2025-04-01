package com.library.librarysystem.service;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BorrowRecord;
import com.library.librarysystem.model.User;
import com.library.librarysystem.repository.BookRepository;
import com.library.librarysystem.repository.BorrowRecordRepository;
import com.library.librarysystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository,
                              BookRepository bookRepository,
                              UserRepository userRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BorrowRecord borrowBook(Long memberId, Long bookId, Long librarianId) {
        // Validate Member
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        if (!"ROLE_MEMBER".equals(member.getRole())) {
            throw new IllegalArgumentException("User is not a member");
        }

        // Validate Librarian
        User librarian = userRepository.findById(librarianId)
                .orElseThrow(() -> new EntityNotFoundException("Librarian not found"));
        if (!"ROLE_LIBRARIAN".equals(librarian.getRole())) {
            throw new IllegalArgumentException("User is not a librarian");
        }

        // Check Book availability
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Create a new borrow record
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setLibrarian(librarian);
        record.setBorrowDate(LocalDate.now());
        record.setReturned(false);
        record.setStatus(BorrowRecord.Status.BORROWED);

        // Update Book availability
        book.setAvailable(false);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    @Override
    public BorrowRecord returnBook(Long borrowRecordId) {
        // Fetch borrow record
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));

        if (record.isReturned()) {
            throw new IllegalStateException("Book already returned");
        }

        // Update status and return data
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowRecord.Status.RETURNED); 

        // Set book availability to true
        Book book = record.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    // Fetches borrow history by Book ID
    @Override
    public List<BorrowRecord> getBorrowHistoryByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return borrowRecordRepository.findByBook(book);
    }

    // Fetches borrow history by Member ID
    @Override
    public List<BorrowRecord> getBorrowHistoryByMember(Long memberId) {
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return borrowRecordRepository.findByMember(member);
    }
}
