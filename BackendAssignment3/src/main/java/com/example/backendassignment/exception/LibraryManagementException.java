package com.example.backendassignment.exception;

import org.springframework.http.HttpStatus;

public class LibraryManagementException extends RuntimeException {

    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    private String errorDescription;

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LibraryManagementException(int errorCode, String errorDescription, HttpStatus httpStatus) {

        super(errorDescription);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.httpStatus = httpStatus;
    }
}
