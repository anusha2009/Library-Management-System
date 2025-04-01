/*package com.library.librarysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.User;
import com.library.librarysystem.service.BookService;
import com.library.librarysystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User librarian;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        librarian = new User(1L, "librarian", "pass", "librarian@mail.com", "First", "Last", "1234567890", "ROLE_LIBRARIAN");
        sampleBook = new Book(1L, "Book", "Author", "ISBN", 2023, true);
        when(userService.findByUsername("librarian")).thenReturn(librarian);
    }

    @Test
    void testAddBook_asLibrarian() throws Exception {
        when(bookService.addBook(any())).thenReturn(sampleBook);

        mockMvc.perform(post("/api/books")
                .with(user("librarian")
                    .password("pass")
                    .roles("LIBRARIAN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Book"));
    }

}
*/