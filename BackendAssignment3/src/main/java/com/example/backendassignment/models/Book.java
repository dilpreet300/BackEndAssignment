package com.example.backendassignment.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "books")

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    private String author;


    private int totalCopies;


    private int copiesAvailable;
    private LocalDate publicationYear;

    private String isbn;

    @OneToMany(mappedBy = "book",cascade = CascadeType.REMOVE)
    List<BorrowingRecord> borrowingRecordOfBook;

    public Book(Long id, String title, String author, int totalCopies, int copiesAvailable, LocalDate publicationYear, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.copiesAvailable = copiesAvailable;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    public Book() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public void setPublicationYear(LocalDate publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public LocalDate getPublicationYear() {
        return publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }
}
