package com.library.librarysystem.repository;

import com.library.librarysystem.model.BorrowRecord;
import com.library.librarysystem.model.User;
import com.library.librarysystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // Returns all borrow records associated with the given member
    List<BorrowRecord> findByMember(User member);
    // Returns all borrow records associated with the given book
    List<BorrowRecord> findByBook(Book book);
}
