package com.library.librarysystem.controller;

import com.library.librarysystem.model.BorrowRecord;
import com.library.librarysystem.service.BorrowService;
import com.library.librarysystem.model.User;
import com.library.librarysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    private UserService userService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    // Get current authenticated user
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }

    // Borrow a book (accessible by both Member and Librarian)
    @PostMapping("/checkout")
    public ResponseEntity<BorrowRecord> borrowBook(
            @RequestParam Long memberId,
            @RequestParam Long bookId,
            @RequestParam Long librarianId
    ) {
        return ResponseEntity.ok(borrowService.borrowBook(memberId, bookId, librarianId));
    }

    // Return a book (accessible by both Member and Librarian)
    @PostMapping("/return")
    public ResponseEntity<BorrowRecord> returnBook(@RequestParam Long borrowRecordId) {
        return ResponseEntity.ok(borrowService.returnBook(borrowRecordId));
    }

    // Get borrow history by member (accessible only by Librarian)
    @GetMapping("/history/member/{memberId}")
    public ResponseEntity<List<BorrowRecord>> getHistoryByMember(@PathVariable Long memberId) {
        User currentUser = getCurrentUser();
        if (!currentUser.isLibrarian()) {
           return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(borrowService.getBorrowHistoryByMember(memberId));
    }

    // Get borrow history by book (accessible only by Librarian)
    @GetMapping("/history/book/{bookId}")
    public ResponseEntity<List<BorrowRecord>> getHistoryByBook(@PathVariable Long bookId) {
        User currentUser = getCurrentUser();
        if (!currentUser.isLibrarian()) {
           return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(borrowService.getBorrowHistoryByBook(bookId));
    }
}
