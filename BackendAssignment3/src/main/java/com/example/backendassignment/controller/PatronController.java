package com.example.backendassignment.controller;

import com.example.backendassignment.dto.PatronDTO;
import com.example.backendassignment.models.Patron;
import com.example.backendassignment.service.PatronService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    // GET /api/patrons
    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();
        List<PatronDTO> patronDTOs = patrons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(patronDTOs, HttpStatus.OK);
    }

    // GET /api/patrons/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        Patron patron = patronService.getPatronById(id);
        PatronDTO patronDTO = convertToDTO(patron);
        return new ResponseEntity<>(patronDTO, HttpStatus.OK);
    }

    // POST /api/patrons
    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@Valid @RequestBody PatronDTO patronDTO) {
        Patron patron = convertToEntity(patronDTO);
        Patron savedPatron = patronService.addPatron(patron);
        PatronDTO savedPatronDTO = convertToDTO(savedPatron);
        return new ResponseEntity<>(savedPatronDTO, HttpStatus.CREATED);
    }

    // PUT /api/patrons/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronDTO updatedPatronDTO) {

        Patron updatedPatron = convertToEntity(updatedPatronDTO);
        updatedPatron.setId(id);

        Patron savedPatron = patronService.updatePatron(id,updatedPatron);
        PatronDTO savedPatronDTO = convertToDTO(savedPatron);
        return new ResponseEntity<>(savedPatronDTO, HttpStatus.OK);
    }

    // DELETE /api/patrons/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {

        patronService.deletePatron(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Helper method to convert Patron entity to PatronDTO
    private PatronDTO convertToDTO(Patron patron) {
        PatronDTO patronDTO = new PatronDTO();
        BeanUtils.copyProperties(patron, patronDTO);
        return patronDTO;
    }

    // Helper method to convert PatronDTO to Patron entity
    private Patron convertToEntity(PatronDTO patronDTO) {
        Patron patron = new Patron();
        BeanUtils.copyProperties(patronDTO, patron);
        return patron;
    }
}
