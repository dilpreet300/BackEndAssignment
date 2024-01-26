package com.example.backendassignment.service;

import com.example.backendassignment.exception.ErrorResponseCode;
import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.models.BorrowingRecord;
import com.example.backendassignment.models.Patron;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository mockBorrowingRecordRepository;
    @Mock
    private BookService mockBookService;
    @Mock
    private PatronService mockPatronService;

    @InjectMocks
    private BorrowingRecordService borrowingRecordServiceUnderTest;


    @Test
    void testBorrowBook_BookServiceGetBookByIdReturnsNull() {
        // Setup
        when(mockBookService.getBookById(0L)).thenReturn(null);
        final Patron patron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronService.getPatronById(0L)).thenReturn(patron);

        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.borrowBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testBorrowBook_PatronServiceReturnsNull() {
        final Book book = new Book();
        book.setId(0L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(0);
        book.setCopiesAvailable(0);
        when(mockBookService.getBookById(0L)).thenReturn(book);
        when(mockPatronService.getPatronById(0L)).thenReturn(null);
        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.borrowBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testBorrowBook_PatronServiceThrowsLibraryManagementException() {
        final Book book = new Book();
        book.setId(0L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(1);
        book.setCopiesAvailable(1);
        when(mockBookService.getBookById(0L)).thenReturn(book);

        when(mockPatronService.getPatronById(0L)).thenThrow(LibraryManagementException.class);

        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.borrowBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }


    @Test
    void testReturnBook_BookServiceGetBookByIdReturnsNull() {
        when(mockBookService.getBookById(0L)).thenReturn(null);
        final Patron patron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronService.getPatronById(0L)).thenReturn(patron);
        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.returnBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testReturnBook_PatronServiceReturnsNull() {
        final Book book = new Book();
        book.setId(0L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(1);
        book.setCopiesAvailable(1);
        when(mockBookService.getBookById(0L)).thenReturn(book);
        when(mockPatronService.getPatronById(0L)).thenReturn(null);
        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.returnBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testReturnBook_PatronServiceThrowsLibraryManagementException() {
        final Book book = new Book();
        book.setId(0L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(1);
        book.setCopiesAvailable(1);
        when(mockBookService.getBookById(0L)).thenReturn(book);
        when(mockPatronService.getPatronById(0L)).thenThrow(LibraryManagementException.class);
        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.returnBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testReturnBook_BorrowingRecordRepositoryFindByBookAndPatronAndReturnDateIsNullReturnsAbsent() {
        final Book book = new Book();
        book.setId(0L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(1);
        book.setCopiesAvailable(1);
        when(mockBookService.getBookById(0L)).thenReturn(book);

        final Patron patron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronService.getPatronById(0L)).thenReturn(patron);

        when(mockBorrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(any(Book.class),
                any(Patron.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.returnBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testReturnBook_BookServiceUpdateBookThrowsLibraryManagementException() {
        final Book book = new Book();
        book.setId(0L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setTotalCopies(0);
        book.setCopiesAvailable(0);
        when(mockBookService.getBookById(0L)).thenReturn(book);

        final Patron patron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronService.getPatronById(0L)).thenReturn(patron);

        final BorrowingRecord borrowingRecord1 = new BorrowingRecord();
        final Patron patron1 = new Patron();
        borrowingRecord1.setPatron(patron1);
        final Book book1 = new Book();
        book1.setId(0L);
        book1.setCopiesAvailable(0);
        borrowingRecord1.setBook(book1);
        borrowingRecord1.setBorrowDate(LocalDate.of(2020, 1, 1));
        borrowingRecord1.setReturnDate(LocalDate.of(2020, 1, 1));
        final Optional<BorrowingRecord> borrowingRecord = Optional.of(borrowingRecord1);
        when(mockBorrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(any(Book.class),
                any(Patron.class))).thenReturn(borrowingRecord);
        when(mockBookService.updateBook(eq(0L), any(Book.class))).thenThrow(LibraryManagementException.class);
        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.returnBook(0L, 0L))
                .isInstanceOf(LibraryManagementException.class);
    }


    @Test
    void testGetAllBorrowingRecords_BorrowingRecordRepositoryReturnsNoItems() {
        when(mockBorrowingRecordRepository.findAll()).thenReturn(Collections.emptyList());
        final List<BorrowingRecord> result = borrowingRecordServiceUnderTest.getAllBorrowingRecords();
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testBorrowBook_SuccessfulBorrowing() throws LibraryManagementException {
        final long bookId = 1L;
        final long patronId = 2L;
        final Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setCopiesAvailable(1);
        book.setTotalCopies(1);
        final Patron patron = new Patron();
        patron.setId(patronId);
        patron.setFirstName("John");
        patron.setLastName("Doe");
        final BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());

        when(mockBookService.getBookById(bookId)).thenReturn(book);
        when(mockPatronService.getPatronById(patronId)).thenReturn(patron);
        when(mockBorrowingRecordRepository.existsByBookAndPatronAndReturnDateIsNull(book, patron)).thenReturn(false);
        when(mockBorrowingRecordRepository.save(any(BorrowingRecord.class) )).thenReturn(borrowingRecord);
        BorrowingRecord result = borrowingRecordServiceUnderTest.borrowBook(bookId, patronId);

        verify(mockBookService).updateBook(eq(bookId), any(Book.class));
        verify(mockBorrowingRecordRepository).save(any(BorrowingRecord.class));

        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(result.getBook()).isEqualTo(book);
        AssertionsForClassTypes.assertThat(result.getPatron()).isEqualTo(patron);
        AssertionsForClassTypes.assertThat(result.getBorrowDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void testReturnBook_SuccessfulReturn() throws LibraryManagementException {
        final long bookId = 1L;
        final long patronId = 2L;

        final Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setCopiesAvailable(1);
        book.setTotalCopies(1);

        final Patron patron = new Patron();
        patron.setId(patronId);
        patron.setFirstName("John");
        patron.setLastName("Doe");

        final BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecord.setReturnDate(LocalDate.now());

        when(mockBookService.getBookById(bookId)).thenReturn(book);
        when(mockPatronService.getPatronById(patronId)).thenReturn(patron);
        when(mockBorrowingRecordRepository.save(any(BorrowingRecord.class) )).thenReturn(borrowingRecord);
        when(mockBorrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(any(Book.class),any(Patron.class))).thenReturn(Optional.of(borrowingRecord));

        BorrowingRecord result = borrowingRecordServiceUnderTest.returnBook(bookId,patronId);

        verify(mockBookService).updateBook(eq(bookId), any(Book.class));
        verify(mockBorrowingRecordRepository).save(any(BorrowingRecord.class));

        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(result.getBook()).isEqualTo(book);
        AssertionsForClassTypes.assertThat(result.getPatron()).isEqualTo(patron);
        AssertionsForClassTypes.assertThat(result.getBorrowDate()).isEqualTo(LocalDate.now());
    }
    @Test
    void testBorrowBook_WhenHasAlreadyBorrowed() {
        Long bookId = 1L;
        Long patronId = 2L;
        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setCopiesAvailable(1);
        Patron mockPatron = new Patron();
        mockPatron.setId(patronId);
        when(mockBookService.getBookById(any(Long.class))).thenReturn(mockBook);
        when(mockPatronService.getPatronById(any(Long.class))).thenReturn(mockPatron);
        when(mockBorrowingRecordRepository.existsByBookAndPatronAndReturnDateIsNull(any(Book.class), any(Patron.class)))
                .thenReturn(true);

        LibraryManagementException exception = assertThrows(LibraryManagementException.class, () -> {
            borrowingRecordServiceUnderTest.borrowBook(bookId, patronId);
        });

        assertEquals(ErrorResponseCode.HAS_ALREADY_BORROWED.getCode(), exception.getErrorCode());
        assertEquals(ErrorResponseCode.HAS_ALREADY_BORROWED.getDescription(), exception.getErrorDescription());
        assertEquals(ErrorResponseCode.HAS_ALREADY_BORROWED.getHttpStatus(), exception.getHttpStatus());

        verify(mockBookService, never()).updateBook(any(Long.class), any(Book.class));
        verify(mockBorrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_WhenNoCopyAvailable() {
        Long bookId = 1L;
        Long patronId = 2L;

        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setCopiesAvailable(0);
        Patron mockPatron = new Patron();
        mockPatron.setId(patronId);

        when(mockBookService.getBookById(any(Long.class))).thenReturn(mockBook);
        when(mockPatronService.getPatronById(any(Long.class))).thenReturn(mockPatron);
        LibraryManagementException exception = assertThrows(LibraryManagementException.class, () -> {
            borrowingRecordServiceUnderTest.borrowBook(bookId, patronId);
        });

        assertEquals(ErrorResponseCode.NO_COPY_AVAILABLE.getCode(), exception.getErrorCode());
        assertEquals(ErrorResponseCode.NO_COPY_AVAILABLE.getDescription(), exception.getErrorDescription());
        assertEquals(ErrorResponseCode.NO_COPY_AVAILABLE.getHttpStatus(), exception.getHttpStatus());

        verify(mockBookService, never()).updateBook(any(Long.class), any(Book.class));
        verify(mockBorrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }
    @Test
    void testBorrowBook_BookNotFound() {
        final long nonExistingBookId = 99L;
        final long patronId = 2L;

        when(mockBookService.getBookById(nonExistingBookId)).thenReturn(null);
        AssertionsForClassTypes.assertThatThrownBy(() -> borrowingRecordServiceUnderTest.borrowBook(nonExistingBookId, patronId))
                .isInstanceOf(LibraryManagementException.class)
                .hasMessageContaining("Searched Book Does not exist");
    }

}
