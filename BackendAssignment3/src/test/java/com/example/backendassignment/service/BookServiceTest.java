package com.example.backendassignment.service;

import com.example.backendassignment.exception.ErrorResponseCode;
import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.repository.BookRepository;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository mockBookRepository;
    @Mock
    private BorrowingRecordRepository mockBorrowingRecordRepository;

    @InjectMocks
    private BookService bookServiceUnderTest;

    @Test
    void testGetAllBooks() {
        // Setup
        // Configure BookRepository.findAll(...).
        final Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(0);
        book.setCopiesAvailable(0);
        book.setIsbn("isbn");
        final List<Book> books = Arrays.asList(book);
        when(mockBookRepository.findAll()).thenReturn(books);

        // Run the test
        final List<Book> result = bookServiceUnderTest.getAllBooks();

        // Verify the results
    }

    @Test
    void testGetAllBooks_BookRepositoryReturnsNoItems() {
        // Setup
        when(mockBookRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Book> result = bookServiceUnderTest.getAllBooks();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetBookById() {
        // Setup
        // Configure BookRepository.findById(...).
        final Book book1 = new Book();
        book1.setTitle("title");
        book1.setAuthor("author");
        book1.setTotalCopies(0);
        book1.setCopiesAvailable(0);
        book1.setIsbn("isbn");
        final Optional<Book> book = Optional.of(book1);
        when(mockBookRepository.findById(0L)).thenReturn(book);

        // Run the test
        final Book result = bookServiceUnderTest.getBookById(0L);

        // Verify the results
    }

    @Test
    void testGetBookById_BookRepositoryReturnsAbsent() {
        // Setup
        when(mockBookRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> bookServiceUnderTest.getBookById(0L)).isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testAddBook() {
        // Setup
        final Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(1);
        book.setCopiesAvailable(1);
        book.setIsbn("isbn");

        when(mockBookRepository.existsByIsbn("isbn")).thenReturn(false);

        // Configure BookRepository.save(...).
        final Book book1 = new Book();
        book1.setTitle("title");
        book1.setAuthor("author");
        book1.setTotalCopies(0);
        book1.setCopiesAvailable(0);
        book1.setIsbn("isbn");
        when(mockBookRepository.save(any(Book.class))).thenReturn(book1);

        // Run the test
        final Book result = bookServiceUnderTest.addBook(book);

        // Verify the results
    }

    @Test
    void testAddBook_WithAvailableCopiesMoreThanTotalCopies() {
        // Create a book with available copies greater than total copies
        Book book = new Book();
        book.setIsbn("123456789");
        book.setTotalCopies(5);
        book.setCopiesAvailable(10);

        // Mock the behavior of bookRepository.existsByIsbn
        when(mockBookRepository.existsByIsbn(any(String.class))).thenReturn(false);

        // Use assertThrows to verify that a LibraryManagementException is thrown
        LibraryManagementException exception = assertThrows(LibraryManagementException.class, () -> {
            bookServiceUnderTest.addBook(book);
        });

        assertEquals(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getCode(), exception.getErrorCode());
        assertEquals(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getDescription(), exception.getErrorDescription());
        assertEquals(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getHttpStatus(), exception.getHttpStatus());

        // Verify that bookRepository.save() is not called in this case
        verify(mockBookRepository, never()).save(any(Book.class));
    }

    @Test
    void testUpdateBook_WithAvailableCopiesMoreThanTotalCopies() {
        // Create an existing book with available copies greater than total copies
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setIsbn("123456789");
        existingBook.setTotalCopies(5);
        existingBook.setCopiesAvailable(5);

        // Create an updated book with potentially modified properties
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("987654321");
        updatedBook.setCopiesAvailable(10);


        when(mockBookRepository.findById(any(Long.class))).thenReturn(Optional.of(existingBook));

        // Use assertThrows to verify that a LibraryManagementException is thrown
        LibraryManagementException exception = assertThrows(LibraryManagementException.class, () -> {
            bookServiceUnderTest.updateBook(1L, updatedBook);
        });


        assertEquals(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getCode(), exception.getErrorCode());
        assertEquals(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getDescription(), exception.getErrorDescription());
        assertEquals(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getHttpStatus(), exception.getHttpStatus());


        verify(mockBookRepository, never()).save(any(Book.class));
    }

    // Add more test cases as needed to cover other scenarios


    @Test
    void testAddBook_BookRepositoryExistsByIsbnReturnsTrue() {
        // Setup
        final Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(0);
        book.setCopiesAvailable(0);
        book.setIsbn("isbn");

        when(mockBookRepository.existsByIsbn("isbn")).thenReturn(true);

        // Run the test
        assertThatThrownBy(() -> bookServiceUnderTest.addBook(book)).isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testUpdateBook() {
        // Setup
        final Book updatedBook = new Book();
        updatedBook.setTitle("title");
        updatedBook.setAuthor("author");
        updatedBook.setTotalCopies(0);
        updatedBook.setCopiesAvailable(0);
        updatedBook.setIsbn("isbn");

        // Configure BookRepository.findById(...).
        final Book book1 = new Book();
        book1.setTitle("title");
        book1.setAuthor("author");
        book1.setTotalCopies(0);
        book1.setCopiesAvailable(0);
        book1.setIsbn("isbn");
        final Optional<Book> book = Optional.of(book1);
        when(mockBookRepository.findById(0L)).thenReturn(book);

        // Configure BookRepository.save(...).
        final Book book2 = new Book();
        book2.setTitle("title");
        book2.setAuthor("author");
        book2.setTotalCopies(0);
        book2.setCopiesAvailable(0);
        book2.setIsbn("isbn");
        when(mockBookRepository.save(any(Book.class))).thenReturn(book2);

        // Run the test
        final Book result = bookServiceUnderTest.updateBook(0L, updatedBook);

        // Verify the results
    }

    @Test
    void testUpdateBook_BookRepositoryFindByIdReturnsAbsent() {
        // Setup
        final Book updatedBook = new Book();
        updatedBook.setTitle("title");
        updatedBook.setAuthor("author");
        updatedBook.setTotalCopies(1);
        updatedBook.setCopiesAvailable(1);
        updatedBook.setIsbn("isbn");

        when(mockBookRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> bookServiceUnderTest.updateBook(0L, updatedBook))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testDeleteBook() {
        // Setup
        // Configure BookRepository.findById(...).
        final Book book1 = new Book();
        book1.setTitle("title");
        book1.setAuthor("author");
        book1.setTotalCopies(0);
        book1.setCopiesAvailable(0);
        book1.setIsbn("isbn");
        final Optional<Book> book = Optional.of(book1);
        when(mockBookRepository.findById(0L)).thenReturn(book);

        when(mockBorrowingRecordRepository.existsByBookAndReturnDateIsNull(any(Book.class))).thenReturn(false);

        // Run the test
        bookServiceUnderTest.deleteBook(0L);

        // Verify the results
        verify(mockBookRepository).deleteById(0L);
    }

    @Test
    void testDeleteBook_BookRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockBookRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> bookServiceUnderTest.deleteBook(0L)).isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testDeleteBook_BorrowingRecordRepositoryReturnsTrue() {
        // Setup
        // Configure BookRepository.findById(...).
        final Book book1 = new Book();
        book1.setTitle("title");
        book1.setAuthor("author");
        book1.setTotalCopies(0);
        book1.setCopiesAvailable(0);
        book1.setIsbn("isbn");
        final Optional<Book> book = Optional.of(book1);
        when(mockBookRepository.findById(0L)).thenReturn(book);

        when(mockBorrowingRecordRepository.existsByBookAndReturnDateIsNull(any(Book.class))).thenReturn(true);

        // Run the test
        assertThatThrownBy(() -> bookServiceUnderTest.deleteBook(0L)).isInstanceOf(LibraryManagementException.class);
    }
}
