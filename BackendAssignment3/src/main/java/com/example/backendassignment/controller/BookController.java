package com.example.backendassignment.controller;


import com.example.backendassignment.dto.BookDTO;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.repository.BookRepository;
import com.example.backendassignment.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET /api/books
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookDTO> bookDTOs = books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        BookDTO bookDTO = convertToDTO(book);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookDTO> addBook( @Valid @RequestBody BookDTO bookDTO) {

        Book book = convertToEntity(bookDTO);
        Book savedBook = bookService.addBook(book);
        BookDTO savedBookDTO = convertToDTO(savedBook);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO updatedBookDTO) {

        Book updatedBook = convertToEntity(updatedBookDTO);
        updatedBook.setId(id);
        Book savedBook = bookService.updateBook(id,updatedBook);
        BookDTO savedBookDTO = convertToDTO(savedBook);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.OK);
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Helper method to convert Book entity to BookDTO
    private BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(book, bookDTO);
        return bookDTO;
    }

    // Helper method to convert BookDTO to Book entity
    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        return book;
    }

}
