package com.example.backendassignment.service;
import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.models.BorrowingRecord;
import com.example.backendassignment.models.Patron;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest2 {

    @Mock
    private BorrowingRecordRepository mockBorrowingRecordRepository;
    @Mock
    private BookService mockBookService;
    @Mock
    private PatronService mockPatronService;

    @InjectMocks
    private BorrowingRecordService borrowingRecordServiceUnderTest;

    @Test
    void testBorrowBook_SuccessfulBorrowing() throws LibraryManagementException {
        // Mock data
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
        //borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());

        when(mockBookService.getBookById(bookId)).thenReturn(book);
        when(mockPatronService.getPatronById(patronId)).thenReturn(patron);
        when(mockBorrowingRecordRepository.existsByBookAndPatronAndReturnDateIsNull(book, patron)).thenReturn(false);
        when(mockBorrowingRecordRepository.save(any(BorrowingRecord.class) )).thenReturn(borrowingRecord);
        // Run the test
        BorrowingRecord result = borrowingRecordServiceUnderTest.borrowBook(bookId, patronId);

        // Verify
        verify(mockBookService).updateBook(eq(bookId), any(Book.class));
        verify(mockBorrowingRecordRepository).save(any(BorrowingRecord.class));

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getBook()).isEqualTo(book);
        assertThat(result.getPatron()).isEqualTo(patron);
        assertThat(result.getBorrowDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void testBorrowBook_BookNotFound() {
        // Mock data
        final long nonExistingBookId = 99L;
        final long patronId = 2L;

        when(mockBookService.getBookById(nonExistingBookId)).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> borrowingRecordServiceUnderTest.borrowBook(nonExistingBookId, patronId))
                .isInstanceOf(LibraryManagementException.class)
                .hasMessageContaining("Book not found");
    }

    // Similar tests for PatronNotFound, HasAlreadyBorrowed, NoCopyAvailable scenarios
    // ...

    // Additional test cases can be added to cover different scenarios
}
