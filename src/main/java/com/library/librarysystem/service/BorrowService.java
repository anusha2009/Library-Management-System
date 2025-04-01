package com.library.librarysystem.service;

import com.library.librarysystem.model.BorrowRecord;

import java.util.List;

public interface BorrowService {
    BorrowRecord borrowBook(Long memberId, Long bookId, Long librarianId);
    BorrowRecord returnBook(Long borrowRecordId);
    List<BorrowRecord> getBorrowHistoryByMember(Long memberId);
    List<BorrowRecord> getBorrowHistoryByBook(Long bookId);
}
