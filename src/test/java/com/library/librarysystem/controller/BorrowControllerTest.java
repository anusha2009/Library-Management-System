/*package com.library.librarysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BorrowRecord;
import com.library.librarysystem.model.User;
import com.library.librarysystem.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowController.class)
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowService borrowService;

    @Autowired
    private ObjectMapper objectMapper;

    private BorrowRecord record;

    @BeforeEach
    void setUp() {
        User member = new User(1L, "member", "pass", "member@mail.com", "First", "Last", "1234567890", "ROLE_MEMBER");
        User librarian = new User(2L, "librarian", "pass", "librarian@mail.com", "First", "Last", "1234567890", "ROLE_LIBRARIAN");
        Book book = new Book(1L, "Book", "Author", "123", 2020, true);
        record = new BorrowRecord(false, 1L, member, librarian, book, LocalDate.now(), null, BorrowRecord.Status.BORROWED);
    }

    @Test
    @WithMockUser(username = "lib", roles = "LIBRARIAN")
    void testBorrowBook() throws Exception {
        when(borrowService.borrowBook(1L, 1L, 2L)).thenReturn(record);

        mockMvc.perform(post("/api/borrow/checkout")
                        .param("memberId", "1")
                        .param("bookId", "1")
                        .param("librarianId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(record.getId()));
    }

    @Test
    @WithMockUser(username = "lib", roles = "LIBRARIAN")
    void testReturnBook() throws Exception {
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());
        when(borrowService.returnBook(1L)).thenReturn(record);

        mockMvc.perform(post("/api/borrow/return")
                        .param("borrowRecordId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned").value(true));
    }
}
*/