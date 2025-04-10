package com.library.librarysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarysystem.config.SecurityConfig;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BorrowRecord;
import com.library.librarysystem.model.User;
import com.library.librarysystem.security.CustomUserDetailsService;
import com.library.librarysystem.service.BorrowService;
import com.library.librarysystem.service.UserService;
import com.library.librarysystem.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowController.class)
@Import({SecurityConfig.class})
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowService borrowService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthUtil authUtil;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private User member;
    private User librarian;
    private Book book;
    private BorrowRecord borrowRecord;

    @BeforeEach
    void setUp() {
        member = new User(2L, "member", "password", "First", "Last", "member@mail.com", "9999999999", "ROLE_MEMBER");
        librarian = new User(1L, "librarian", "password", "First", "Last", "librarian@mail.com", "1111111111", "ROLE_LIBRARIAN");
        book = new Book(3L, "Clean Code", "Robert C. Martin", "ISBN123", 2008, true);
        borrowRecord = new BorrowRecord(false, 1L, member, librarian, book, LocalDate.now(), null, BorrowRecord.Status.BORROWED);
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testBorrowBook() throws Exception {
        when(borrowService.borrowBook(2L, 3L, 1L)).thenReturn(borrowRecord);

        mockMvc.perform(post("/api/borrow/checkout")
                        .param("memberId", "2")
                        .param("bookId", "3")
                        .param("librarianId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "member", roles = "MEMBER")
    void testReturnBook() throws Exception {
        when(borrowService.returnBook(1L)).thenReturn(borrowRecord);

        mockMvc.perform(post("/api/borrow/return")
                        .param("borrowRecordId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
