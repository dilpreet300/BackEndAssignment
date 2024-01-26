package com.example.backendassignment.repository;
import com.example.backendassignment.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {

    // Find a patron by ID
    Optional<Patron> findById(Long id);

    // Check if a patron exists by ID
    boolean existsById(Long id);

    // Save a patron
    <S extends Patron> S save(S patron);

    // Delete a patron by ID
    void deleteById(Long id);

    boolean existsByEmail(String email);
}
