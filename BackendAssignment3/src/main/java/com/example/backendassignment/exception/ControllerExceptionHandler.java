package com.example.backendassignment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    // Exception handler for BorrowingException
    @ExceptionHandler(LibraryManagementException.class)
    public ResponseEntity<ErrorMessage> handleException(LibraryManagementException ex) {

        ErrorMessage message = new ErrorMessage(
                ex.getErrorCode(),
                new Date(),
                ex.getErrorDescription());

        return new ResponseEntity<ErrorMessage>(message,ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();

        // Collect field-specific validation errors
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorDetails = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errorDetails.add(field + ": " + message);
        }
        ErrorMessage message = new ErrorMessage(
                ErrorResponseCode.CONSTRAINT_VIOLATION_EXCEPTION.getCode(),
                new Date(),
                errorDetails.toString());

        return new ResponseEntity<ErrorMessage>(message,ErrorResponseCode.CONSTRAINT_VIOLATION_EXCEPTION.getHttpStatus());
    }
    // Global exception handler for all unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception ex) {

        ErrorMessage message = new ErrorMessage(
                ErrorResponseCode.NEW_ERROR.getCode(),
                new Date(),
                ex.getMessage());
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
