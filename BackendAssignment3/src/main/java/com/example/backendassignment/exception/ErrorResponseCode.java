package com.example.backendassignment.exception;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;


public enum ErrorResponseCode {

    BOOK_NOT_FOUND(1,"Searched Book Does not exist ",HttpStatus.BAD_REQUEST),
    PATRON_NOT_FOUND(2,"Searched Patron Does not exist ",HttpStatus.BAD_REQUEST),
    HAS_ALREADY_BORROWED(3,"Patron has already borrowed this book ",HttpStatus.BAD_REQUEST),
    NO_COPY_AVAILABLE(4,"No Copies are available ",HttpStatus.BAD_REQUEST),

    NO_BORROWING_RECORD(5,"Patron has not borrowed book ",HttpStatus.BAD_REQUEST),

    BOOK_HAS_ACTIVE_BORROWING_RECORD(6,"Book has active borrow record can't be deleted",HttpStatus.BAD_REQUEST),

    PATRON_HAS_ACTIVE_BORROWING_RECORD(7,"Patron has active borrow record can't be deleted",HttpStatus.BAD_REQUEST),

    AVAILABLE_COPIES_MORE_THAN_TOTAL_COPIES(8,"Available copies more than total copies can't save",HttpStatus.BAD_REQUEST),

    CONSTRAINT_VIOLATION_EXCEPTION(9,"one or more attributes are invalid",HttpStatus.BAD_REQUEST),

    BOOK_ALREADY_PRESENT(10,"Book already present , can't create another entry , if possible try to update it",HttpStatus.BAD_REQUEST),
    PATRON_EMAIL_ALREADY_EXISTS(11,"Patron already present corresponding to this email",HttpStatus.BAD_REQUEST),

    NEW_ERROR(-1,"Some Unexpected error has occured",HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;



    ErrorResponseCode(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


}
