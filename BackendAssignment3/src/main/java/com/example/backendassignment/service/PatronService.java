package com.example.backendassignment.service;

import com.example.backendassignment.exception.ErrorResponseCode;
import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Patron;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import com.example.backendassignment.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }
    public Patron getPatronById(Long id) throws LibraryManagementException{
        Optional<Patron> optionalPatron = patronRepository.findById(id);
        if(optionalPatron.isPresent()) {
            return optionalPatron.get();
        } else{
            throw new LibraryManagementException(ErrorResponseCode.PATRON_NOT_FOUND.getCode(),ErrorResponseCode.PATRON_NOT_FOUND.getDescription(),
                    ErrorResponseCode.PATRON_NOT_FOUND.getHttpStatus());
        }
    }
    public Patron addPatron(Patron patron) {
        // You can add additional validation or business logic before saving
        boolean emailExits = patronRepository.existsByEmail(patron.getEmail());
        if(emailExits){
            throw new LibraryManagementException(ErrorResponseCode.PATRON_EMAIL_ALREADY_EXISTS.getCode(),ErrorResponseCode.PATRON_EMAIL_ALREADY_EXISTS.getDescription(),
                    ErrorResponseCode.PATRON_EMAIL_ALREADY_EXISTS.getHttpStatus());
        }
        return patronRepository.save(patron);
    }
    public Patron updatePatron(Long id, Patron updatedPatron) throws LibraryManagementException {
        Optional<Patron> optionalExistingPatron = patronRepository.findById(id);
        if (optionalExistingPatron.isPresent()) {
            Patron existingPatron = optionalExistingPatron.get();
            existingPatron.setFirstName(updatedPatron.getFirstName());
            existingPatron.setLastName(updatedPatron.getLastName());
            existingPatron.setEmail(updatedPatron.getEmail());

            return patronRepository.save(existingPatron);
        } else {
            throw new LibraryManagementException(ErrorResponseCode.PATRON_NOT_FOUND.getCode(),ErrorResponseCode.PATRON_NOT_FOUND.getDescription(),
                    ErrorResponseCode.PATRON_NOT_FOUND.getHttpStatus());
        }
    }
    public void deletePatron(Long id) throws LibraryManagementException {
        Optional<Patron>  patron = patronRepository.findById(id);
        if (patron.isPresent()) {
            boolean hasActiveBorrowingRecord = borrowingRecordRepository.existsByPatronAndReturnDateIsNull(patron.get());
            if(hasActiveBorrowingRecord){
                throw new LibraryManagementException(ErrorResponseCode.PATRON_HAS_ACTIVE_BORROWING_RECORD.getCode(),ErrorResponseCode.PATRON_HAS_ACTIVE_BORROWING_RECORD.getDescription(),
                        ErrorResponseCode.PATRON_HAS_ACTIVE_BORROWING_RECORD.getHttpStatus());
            }
            else{
                patronRepository.deleteById(id);
            }
        } else {
            throw new LibraryManagementException(ErrorResponseCode.PATRON_NOT_FOUND.getCode(),ErrorResponseCode.PATRON_NOT_FOUND.getDescription(),
                    ErrorResponseCode.PATRON_NOT_FOUND.getHttpStatus());
        }
    }
}