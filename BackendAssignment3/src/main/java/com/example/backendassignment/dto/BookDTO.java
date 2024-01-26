package com.example.backendassignment.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

//@Data
public class BookDTO {

    private long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @Min(1)
    private int totalCopies;
    @Min(0)
    private int copiesAvailable;
    @Past
    private LocalDate publicationYear;
    @NotBlank
    private String isbn;

    // Constructors, getters, setters, etc.


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public BookDTO(long id, String title, String author, int totalCopies, int copiesAvailable, LocalDate publicationYear, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.copiesAvailable = copiesAvailable;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
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

    public BookDTO() {
    }
}
