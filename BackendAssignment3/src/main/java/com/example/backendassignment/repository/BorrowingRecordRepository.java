package com.example.backendassignment.repository;

import com.example.backendassignment.models.BorrowingRecord;
import com.example.backendassignment.models.Book;
import com.example.backendassignment.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    // Find all borrowing records
    List<BorrowingRecord> findAll();

    // Find an active borrowing record by book, patron, and return date is null
    Optional<BorrowingRecord> findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);

    // Check if there exists an active borrowing record by book, patron, and return date is null
    boolean existsByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);

    // Save a borrowing record
   // <S extends BorrowingRecord S save(S borrowingRecord);

    // Delete a borrowing record by ID
    void deleteById(Long id);

    boolean existsByBookAndReturnDateIsNull(Book book);

    boolean existsByPatronAndReturnDateIsNull(Patron patron);
}


