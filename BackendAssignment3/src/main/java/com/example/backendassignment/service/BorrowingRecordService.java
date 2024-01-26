package com.example.backendassignment.service;

import com.example.backendassignment.exception.ErrorResponseCode;
import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.models.BorrowingRecord;
import com.example.backendassignment.models.Patron;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private PatronService patronService;


    //method to borrow book corresponding to bookId and patronID
    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) throws LibraryManagementException {
        Book book = bookService.getBookById(bookId);
        Patron patron = patronService.getPatronById(patronId);

        if (book == null) {
            throw new LibraryManagementException(ErrorResponseCode.BOOK_NOT_FOUND.getCode(),
                                                    ErrorResponseCode.BOOK_NOT_FOUND.getDescription(),ErrorResponseCode.BOOK_NOT_FOUND.getHttpStatus());
        }
        if (patron == null) {
            throw new LibraryManagementException(ErrorResponseCode.PATRON_NOT_FOUND.getCode(),ErrorResponseCode.PATRON_NOT_FOUND.getDescription(),
                    ErrorResponseCode.PATRON_NOT_FOUND.getHttpStatus());
        }

        // checking if copies are available
        if (book.getCopiesAvailable() > 0) {
            boolean hasActiveBorrowingRecord = borrowingRecordRepository.existsByBookAndPatronAndReturnDateIsNull(book, patron);

            // checking if book there is available borrow record
            if (!hasActiveBorrowingRecord) {
                BorrowingRecord borrowingRecord = new BorrowingRecord();
                borrowingRecord.setBook(book);
                borrowingRecord.setPatron(patron);
                borrowingRecord.setBorrowDate(LocalDate.now());

                book.setCopiesAvailable(book.getCopiesAvailable() - 1);
                bookService.updateBook(book.getId(), book);

                return borrowingRecordRepository.save(borrowingRecord);
            } else {
                throw new LibraryManagementException(ErrorResponseCode.HAS_ALREADY_BORROWED.getCode(),
                        ErrorResponseCode.HAS_ALREADY_BORROWED.getDescription(),ErrorResponseCode.HAS_ALREADY_BORROWED.getHttpStatus());
            }
        } else {
            throw new LibraryManagementException(ErrorResponseCode.NO_COPY_AVAILABLE.getCode(),
                    ErrorResponseCode.NO_COPY_AVAILABLE.getDescription(),ErrorResponseCode.NO_COPY_AVAILABLE.getHttpStatus());
        }
    }


    // return a book corresponding to a bookId and patronId
    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) throws LibraryManagementException {
        Book book = bookService.getBookById(bookId);
        Patron patron = patronService.getPatronById(patronId);

        if (book == null) {
            throw new LibraryManagementException(ErrorResponseCode.BOOK_NOT_FOUND.getCode(),
                    ErrorResponseCode.BOOK_NOT_FOUND.getDescription(),ErrorResponseCode.BOOK_NOT_FOUND.getHttpStatus());
        }
        if (patron == null) {
            throw new LibraryManagementException(ErrorResponseCode.PATRON_NOT_FOUND.getCode(),ErrorResponseCode.PATRON_NOT_FOUND.getDescription(),
                    ErrorResponseCode.PATRON_NOT_FOUND.getHttpStatus());
        }

        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(book, patron).orElse(null);

        if (borrowingRecord != null) {
            borrowingRecord.setReturnDate(LocalDate.now());
            book.setCopiesAvailable(book.getCopiesAvailable() + 1);
            bookService.updateBook(book.getId(), book);

            return borrowingRecordRepository.save(borrowingRecord);
        } else {
            throw new  LibraryManagementException(ErrorResponseCode.NO_BORROWING_RECORD.getCode(),
                    ErrorResponseCode.NO_BORROWING_RECORD.getDescription(),ErrorResponseCode.NO_BORROWING_RECORD.getHttpStatus());
        }
    }

    // to get all borrow records
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }
}
