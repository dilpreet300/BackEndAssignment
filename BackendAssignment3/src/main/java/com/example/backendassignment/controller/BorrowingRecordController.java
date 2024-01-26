package com.example.backendassignment.controller;

import com.example.backendassignment.models.BorrowingRecord;
import com.example.backendassignment.service.BookService;
import com.example.backendassignment.service.BorrowingRecordService;
import com.example.backendassignment.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowingrecords")
public class BorrowingRecordController {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private BookService bookService;

    @Autowired
    private PatronService patronService;

    // POST /api/borrowingrecords/borrow/{bookId}/patron/{patronId}
    @PostMapping("/borrow/{bookId}/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {

            BorrowingRecord borrowedRecord = borrowingRecordService.borrowBook(bookId, patronId);
            return new ResponseEntity<>(borrowedRecord, HttpStatus.CREATED);
    }

    // PUT /api/borrowingrecords/return/{bookId}/{patronId}
    @PutMapping("/return/{bookId}/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
            BorrowingRecord returnedRecord = borrowingRecordService.returnBook(bookId, patronId);
            return new ResponseEntity<>(returnedRecord, HttpStatus.OK);

    }

//     Additional endpoint to get all borrowing records
    @GetMapping
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
        List<BorrowingRecord> borrowingRecords = borrowingRecordService.getAllBorrowingRecords();
        return new ResponseEntity<>(borrowingRecords, HttpStatus.OK);
    }

}
