package com.library.librarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "borrow_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BorrowRecord {
    // Flag to indicate if book is returned
    private boolean returned;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member who borrowed the book
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    // Librarian who processed the borrow activity
    @ManyToOne(optional = false)
    @JoinColumn(name = "librarian_id", nullable = false)
    private User librarian;

    // Borrowed book
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    // Enum to indicate borrow state
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        BORROWED,
        RETURNED
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
