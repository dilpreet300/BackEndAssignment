package com.example.backendassignment.service;

import com.example.backendassignment.exception.ErrorResponseCode;
import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.models.BorrowingRecord;
import com.example.backendassignment.repository.BookRepository;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isPresent()){
            return optionalBook.get();
        }
        else{
            throw new LibraryManagementException(ErrorResponseCode.BOOK_NOT_FOUND.getCode(),
                    ErrorResponseCode.BOOK_NOT_FOUND.getDescription(),ErrorResponseCode.BOOK_NOT_FOUND.getHttpStatus());
        }

    }

    public Book addBook(Book book) {
        boolean bookExists = bookRepository.existsByIsbn(book.getIsbn());
        if(bookExists){
            throw new LibraryManagementException(ErrorResponseCode.BOOK_ALREADY_PRESENT.getCode(),
                    ErrorResponseCode.BOOK_ALREADY_PRESENT.getDescription(),ErrorResponseCode.BOOK_ALREADY_PRESENT.getHttpStatus());
        }
        if(!isAvailableCopiesLessThanTotalCopies(book)){
            throw new LibraryManagementException(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getCode(),
                    ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getDescription(),ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getHttpStatus());
        }
        else {
            return bookRepository.save(book);
        }
    }

    public Book updateBook(Long id, Book updatedBook) throws LibraryManagementException {
        Optional<Book> optionalExistingBook = bookRepository.findById(id);

        if (optionalExistingBook.isPresent()) {
            Book existingBook = optionalExistingBook.get();

            if(!isAvailableCopiesLessThanTotalCopies(updatedBook)){
                throw new LibraryManagementException(ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getCode(),
                        ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getDescription(),ErrorResponseCode.AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES.getHttpStatus());
            }

            else {
                // Update existing book properties
                existingBook.setTitle(updatedBook.getTitle());
                existingBook.setAuthor(updatedBook.getAuthor());
                existingBook.setIsbn(updatedBook.getIsbn());

                // Save the updated book
                return bookRepository.save(existingBook);
            }
        } else {
            throw new LibraryManagementException(ErrorResponseCode.BOOK_NOT_FOUND.getCode(),
                    ErrorResponseCode.BOOK_NOT_FOUND.getDescription(),ErrorResponseCode.BOOK_NOT_FOUND.getHttpStatus());
        }
    }

    public void deleteBook(Long id) throws LibraryManagementException {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {

            boolean hasActiveBorrowingRecord = borrowingRecordRepository.existsByBookAndReturnDateIsNull(book.get());
            if(hasActiveBorrowingRecord){
                throw new LibraryManagementException(ErrorResponseCode.BOOK_HAS_ACTIVE_BORROWING_RECORD.getCode(),
                        ErrorResponseCode.BOOK_HAS_ACTIVE_BORROWING_RECORD.getDescription(),ErrorResponseCode.BOOK_HAS_ACTIVE_BORROWING_RECORD.getHttpStatus());
            }
            else{
                bookRepository.deleteById(id);
            }
        } else {
            throw new LibraryManagementException(ErrorResponseCode.BOOK_NOT_FOUND.getCode(),
                    ErrorResponseCode.BOOK_NOT_FOUND.getDescription(),ErrorResponseCode.BOOK_NOT_FOUND.getHttpStatus());
        }
    }

    private boolean isAvailableCopiesLessThanTotalCopies(Book book){
        return book.getCopiesAvailable() <= book.getTotalCopies();
    }
}
