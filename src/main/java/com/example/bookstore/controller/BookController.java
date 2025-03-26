package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;
    
    
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    
    // Create a new book (POST)
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
    	 logger.info("Received request to add a new book");
         Book savedBook = bookService.addBook(book);
         logger.info("Book added successfully with ID: {}", savedBook.getId());
         return ResponseEntity.ok(savedBook);
    }
    
    // Retrieve a specific book by ID (GET)
 
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    	logger.info("Received request to fetch book with ID: {}", id);
        Book book = bookService.getBookById(id);//Get the book details to the specific book id
        logger.info("Returning book with ID : {}", id);
        return ResponseEntity.ok(book);
    }

   // Retrieve all books (GET)
   /**
 * Retrieve a book by its ID.
 *
 * @param id The ID of the book to retrieve.
 * @return ResponseEntity containing the book if found, or an error if not found.
 */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
    	logger.info("Received request to fetch all books");
        List<Book> books = bookService.getAllBooks();
        logger.info("Returning {} books", books.size());
        return ResponseEntity.ok(books);
    }

   
    // Update an existing book (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
    	logger.info("Received request to update book with ID: {}", id);
        Book books = bookService.updateBook(id, book);
        logger.info("Book with ID {} updated successfully", id);
        return ResponseEntity.ok(books);
    }

    // Delete a book by ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
    	 logger.info("Received request to delete book with ID: {}", id);
         bookService.deleteBook(id);
         logger.info("Book with ID {} deleted successfully", id);
         return ResponseEntity.ok("Book with ID " + id + " deleted successfully.");
    }
    
    
}
