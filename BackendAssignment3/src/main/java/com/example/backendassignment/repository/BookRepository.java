package com.example.backendassignment.repository;
import com.example.backendassignment.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find all books
    List<Book> findAll();

    // Find a book by ID
    Optional<Book> findById(Long id);

    // Check if a book exists by ID
    boolean existsById(Long id);

    // Save a book
    <S extends Book> S save(S book);

    // Delete a book by ID
    void deleteById(Long id);




    boolean existsByTitle(String title);

    boolean existsByIsbn(String isbn);
}

