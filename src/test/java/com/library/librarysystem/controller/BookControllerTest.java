package com.library.librarysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarysystem.config.SecurityConfig;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.User;
import com.library.librarysystem.security.CustomUserDetailsService;
import com.library.librarysystem.service.BookService;
import com.library.librarysystem.service.UserService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private User librarian;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        librarian = new User(1L, "librarian", "password", "First", "Last", "librarian@mail.com", "1234567890", "ROLE_LIBRARIAN");
        sampleBook = new Book(1L, "Book Title", "Author Name", "ISBN123", 2024, true);

        when(userService.findByUsername("librarian")).thenReturn(librarian);
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(sampleBook));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testAddBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(Mockito.eq(1L), any(Book.class))).thenReturn(sampleBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "librarian", roles = "LIBRARIAN")
    void testSearchBooks() throws Exception {
        when(bookService.searchBooks("Book")).thenReturn(List.of(sampleBook));

        mockMvc.perform(get("/api/books/search")
                        .param("keyword", "Book"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "member", roles = "MEMBER")
    void testAddBookException() throws Exception {
        User member = new User(2L, "member", "password", "First", "Last", "member@mail.com", "9876543210", "ROLE_MEMBER");
        when(userService.findByUsername("member")).thenReturn(member);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isForbidden());
    }
}
