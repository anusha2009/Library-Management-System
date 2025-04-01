package com.library.librarysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarysystem.model.User;
import com.library.librarysystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User member, librarian;

    @BeforeEach
    void setUp() {
        member = new User(1L, "member", "pass", "member@mail.com", "First", "Last", "1234567890", "ROLE_MEMBER");
        librarian = new User(2L, "librarian", "pass", "librarian@mail.com", "First", "Last", "1234567890", "ROLE_LIBRARIAN");
    }

    @Test
    void testRegisterMember() throws Exception {
        Mockito.when(userService.registerUser(any())).thenReturn(member);

        mockMvc.perform(post("/register/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_MEMBER"));
    }

    @Test
    void testRegisterLibrarian() throws Exception {
        Mockito.when(userService.registerUser(any())).thenReturn(librarian);

        mockMvc.perform(post("/register/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(librarian)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_LIBRARIAN"));
    }
}
