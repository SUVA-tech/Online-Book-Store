package com.example.bookstore.service;

import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class BookService {

	
    @Autowired
    private BookRepository bookRepository;

    
    private static final Logger logger=LoggerFactory.getLogger(BookService.class);
    // Add a new book
    public Book addBook(Book book) {
    	logger.info("Adding a new book");
        Book savedBook= bookRepository.save(book);
        logger.info("Book added successfully with ID: {}", savedBook.getId());
        return savedBook;
    }

   // Retrieve all books
    public List<Book> getAllBooks() {
    	logger.info("Fetching all books from the database");
        
        List<Book> books = bookRepository.findAll();
        logger.info("Retrieved {} books", books.size());
        return books;
    }
    
   
    // Retrieves a book by its ID, throws an exception if not found
    public Book getBookById(Long id) { // Using Long for ID
        logger.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Book not found with ID: " + id;
                    logger.error(errorMessage);
                    return new BookNotFoundException(errorMessage);
                });
    }

    
    // Update an existing book,throws an exception if it not found
    public Book updateBook(Long id, Book bookDetails) {// Using Long for ID
    	logger.info("Updating book with ID: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    logger.info("Updating details for book ID: {}", id);
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setPrice(bookDetails.getPrice());
                    book.setPublishedDate(bookDetails.getPublishedDate());
                    Book savedBook = bookRepository.save(book);
                    logger.info("Book with ID {} updated successfully", id);
                    return savedBook;
                })
                .orElseThrow(() -> {
                    logger.error("Book with ID {} not found for update", id);
                    return new BookNotFoundException("Book with ID " + id + " not found.");
                });
    }

    // Delete a book by ID,throws an exception if not found
    public void deleteBook(Long id) {
    	logger.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            logger.error("Book with ID {} not found for deletion", id);
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
        bookRepository.deleteById(id);
        logger.info("Book with ID {} deleted successfully", id);
    }
}
