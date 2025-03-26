package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)// Enables Mockito for JUnit 5
class BookControllerTest {
	 // Mocking the BookService
    @Mock
    private BookService bookService;

    //Injecting the mocked bookService into bookController
    @InjectMocks
    private BookController bookController;

    // Test for adding a book
    @Test
    void addBookTest() {
        Book book = new Book(1L, "The Alchemist", "Paulo Coelho", new BigDecimal("350.00"), LocalDate.parse("1988-04-01"));
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.addBook(book);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The Alchemist", response.getBody().getTitle());
    }

    // Test for retrieving all books
    @Test
    void getAllBooksTest() {
        List<Book> books = Arrays.asList(
                new Book(1L, "The Hobbit", "J.R.R. Tolkien", new BigDecimal("450.00"), LocalDate.parse("1937-09-21")),
                new Book(2L, "1984", "George Orwell", new BigDecimal("300.00"), LocalDate.parse("1949-06-08"))
        );
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        assertEquals("The Hobbit", response.getBody().get(0).getTitle());
    }

    // Test for retrieving a book by ID
    @Test
    void getBookByIdTest() {
        Book book = new Book(1L, "To Kill a Mockingbird", "Harper Lee", new BigDecimal("399.00"), LocalDate.parse("1960-07-11"));
        when(bookService.getBookById(1L)).thenReturn(book);

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getBody().getId());
        assertEquals("To Kill a Mockingbird", response.getBody().getTitle());
    }

    // Test for updating a book
    @Test
    void updateBookTest() {
        Book book = new Book(1L, "Original Title", "Unknown Author", new BigDecimal("500.00"), LocalDate.now());
        Book updatedBook = new Book(1L, "Updated Title", "Updated Author", new BigDecimal("550.00"), LocalDate.now());
        when(bookService.updateBook(1L, updatedBook)).thenReturn(updatedBook);

        ResponseEntity<Book> response = bookController.updateBook(1L, updatedBook);

        assertNotNull(response);
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Author", response.getBody().getAuthor());
    }

    // Test for deleting a book
    @Test
    void deleteBookTest() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<String> response = bookController.deleteBook(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Book with ID 1 deleted successfully.", response.getBody());
    }
}
