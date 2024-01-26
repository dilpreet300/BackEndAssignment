package com.example.backendassignment.service;

import com.example.backendassignment.exception.LibraryManagementException;
import com.example.backendassignment.models.Patron;
import com.example.backendassignment.repository.BorrowingRecordRepository;
import com.example.backendassignment.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    private PatronRepository mockPatronRepository;
    @Mock
    private BorrowingRecordRepository mockBorrowingRecordRepository;

    @InjectMocks
    private PatronService patronServiceUnderTest;

    @Test
    void testGetAllPatrons() {
        final List<Patron> patrons = Arrays.asList(new Patron(0L, "firstName", "lastName", "email"));
        when(mockPatronRepository.findAll()).thenReturn(patrons);
        final List<Patron> result = patronServiceUnderTest.getAllPatrons();
    }

    @Test
    void testGetAllPatrons_PatronRepositoryReturnsNoItems() {
        when(mockPatronRepository.findAll()).thenReturn(Collections.emptyList());
        final List<Patron> result = patronServiceUnderTest.getAllPatrons();
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetPatronById() {
        final Optional<Patron> patron = Optional.of(new Patron(0L, "firstName", "lastName", "email"));
        when(mockPatronRepository.findById(0L)).thenReturn(patron);
        final Patron result = patronServiceUnderTest.getPatronById(0L);
    }

    @Test
    void testGetPatronById_PatronRepositoryReturnsAbsent() {
        when(mockPatronRepository.findById(0L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> patronServiceUnderTest.getPatronById(0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testAddPatron() {
        final Patron patron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronRepository.existsByEmail("email")).thenReturn(false);
        final Patron patron1 = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronRepository.save(any(Patron.class))).thenReturn(patron1);
        final Patron result = patronServiceUnderTest.addPatron(patron);
    }

    @Test
    void testAddPatron_PatronRepositoryExistsByEmailReturnsTrue() {
        final Patron patron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronRepository.existsByEmail("email")).thenReturn(true);

        assertThatThrownBy(() -> patronServiceUnderTest.addPatron(patron))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testUpdatePatron() {
        final Patron updatedPatron = new Patron(0L, "firstName", "lastName", "email");
        final Optional<Patron> patron = Optional.of(new Patron(0L, "firstName", "lastName", "email"));
        when(mockPatronRepository.findById(0L)).thenReturn(patron);
        final Patron patron1 = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronRepository.save(any(Patron.class))).thenReturn(patron1);
        final Patron result = patronServiceUnderTest.updatePatron(0L, updatedPatron);

    }

    @Test
    void testUpdatePatron_PatronRepositoryFindByIdReturnsAbsent() {
        final Patron updatedPatron = new Patron(0L, "firstName", "lastName", "email");
        when(mockPatronRepository.findById(0L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> patronServiceUnderTest.updatePatron(0L, updatedPatron))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testDeletePatron() {

        final Optional<Patron> patron = Optional.of(new Patron(0L, "firstName", "lastName", "email"));
        when(mockPatronRepository.findById(0L)).thenReturn(patron);
        when(mockBorrowingRecordRepository.existsByPatronAndReturnDateIsNull(any(Patron.class))).thenReturn(false);
        patronServiceUnderTest.deletePatron(0L);
        verify(mockPatronRepository).deleteById(0L);
    }

    @Test
    void testDeletePatron_PatronRepositoryFindByIdReturnsAbsent() {
        when(mockPatronRepository.findById(0L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> patronServiceUnderTest.deletePatron(0L))
                .isInstanceOf(LibraryManagementException.class);
    }

    @Test
    void testDeletePatron_BorrowingRecordRepositoryReturnsTrue() {
        final Optional<Patron> patron = Optional.of(new Patron(0L, "firstName", "lastName", "email"));
        when(mockPatronRepository.findById(0L)).thenReturn(patron);
        when(mockBorrowingRecordRepository.existsByPatronAndReturnDateIsNull(any(Patron.class))).thenReturn(true);

        assertThatThrownBy(() -> patronServiceUnderTest.deletePatron(0L))
                .isInstanceOf(LibraryManagementException.class);
    }
}
