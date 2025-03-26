package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    // Mocking the BookRepository to simulate database interactions
    @Mock
    private BookRepository bookRepository;

    // Injecting the mock repository into the BookService
    @InjectMocks
    private BookService bookService;

    private final Long VALID_BOOK_ID = 1L;
    private final Long INVALID_BOOK_ID = 100L;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        // Initialize a sample book object before each test
        sampleBook = new Book("Test Book", "Abhay", new BigDecimal("100"), LocalDate.of(2023, 1, 1));
        sampleBook.setId(VALID_BOOK_ID);
    }

    @Test
    void testAddBook() {
        // Simulating successful book saving
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book savedBook = bookService.addBook(sampleBook);

        // Assertions to verify correct behavior
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    void testGetBookById_Found() {
        // Simulating finding a book by valid ID
        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(sampleBook));

        Book book = bookService.getBookById(VALID_BOOK_ID);

        // Assertions to check the correct book is returned
        assertNotNull(book);
        assertEquals(VALID_BOOK_ID, book.getId());
        verify(bookRepository, times(1)).findById(VALID_BOOK_ID);
    }

    @Test
    void testGetBookById_NotFound() {
        // Simulating book not found scenario
        when(bookRepository.findById(INVALID_BOOK_ID)).thenReturn(Optional.empty());

        // Expecting a BookNotFoundException
        Exception exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookById(INVALID_BOOK_ID));
        assertTrue(exception.getMessage().contains("Book not found with ID: " + INVALID_BOOK_ID));
        verify(bookRepository, times(1)).findById(INVALID_BOOK_ID);
    }

    @Test
    void testGetAllBooks() {
        // Simulating fetching all books
        List<Book> books = Arrays.asList(sampleBook, new Book("Another Book", "John Doe", new BigDecimal("150"), LocalDate.of(2023, 2, 15)));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        // Asserting correct number of books returned
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testAddBook_InvalidData() {
        // Simulating invalid book data scenario
        Book invalidBook = new Book();
        when(bookRepository.save(any(Book.class))).thenThrow(new IllegalArgumentException("Invalid book data"));

        // Expecting IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookService.addBook(invalidBook));
        assertTrue(exception.getMessage().contains("Invalid book data"));
    }

    @Test
    void testUpdateBook_Found() {
        // Simulating book update scenario with valid ID
        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book updatedBook = new Book("Updated Book", "Abhay", new BigDecimal("120"), LocalDate.of(2024, 1, 1));
        Book result = bookService.updateBook(VALID_BOOK_ID, updatedBook);

        // Asserting successful update
        assertNotNull(result);
        assertEquals("Updated Book", result.getTitle());
        assertEquals(new BigDecimal("120"), result.getPrice());
        verify(bookRepository, times(1)).findById(VALID_BOOK_ID);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook_NotFound() {
        // Simulating update attempt with invalid book ID
        when(bookRepository.findById(INVALID_BOOK_ID)).thenReturn(Optional.empty());

        Book updatedBook = new Book("Updated Book", "Abhay", new BigDecimal("120"), LocalDate.of(2024, 1, 1));

        // Expecting BookNotFoundException
        Exception exception = assertThrows(BookNotFoundException.class, () -> bookService.updateBook(INVALID_BOOK_ID, updatedBook));
        assertTrue(exception.getMessage().contains("Book with ID " + INVALID_BOOK_ID + " not found."));
        verify(bookRepository, times(1)).findById(INVALID_BOOK_ID);
    }

    @Test
    void testDeleteBook_Found() {
        // Simulating successful deletion of a book
        when(bookRepository.existsById(VALID_BOOK_ID)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(VALID_BOOK_ID);

        assertDoesNotThrow(() -> bookService.deleteBook(VALID_BOOK_ID));
        verify(bookRepository, times(1)).existsById(VALID_BOOK_ID);
        verify(bookRepository, times(1)).deleteById(VALID_BOOK_ID);
    }

    @Test
    void testDeleteBook_NotFound() {
        // Simulating deletion attempt with an invalid book ID
        when(bookRepository.existsById(INVALID_BOOK_ID)).thenReturn(false);

        // Expecting BookNotFoundException
        Exception exception = assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(INVALID_BOOK_ID));
        assertTrue(exception.getMessage().contains("Book with ID " + INVALID_BOOK_ID + " not found."));
        verify(bookRepository, times(1)).existsById(INVALID_BOOK_ID);
    }
} 
