package com.example.backendassignment.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "BorrowingRecord")
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Getter @Setter
    private LocalDate borrowDate;
    @Getter @Setter
    private LocalDate returnDate;

    // Other fields and methods...

}
