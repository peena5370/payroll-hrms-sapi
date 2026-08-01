package com.company.payroll.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.company.payroll.exception.classes.BadRequestException;
import com.company.payroll.exception.classes.DuplicateResourceException;
import com.company.payroll.exception.classes.InternalServerErrorException;
import com.company.payroll.exception.classes.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler({Exception.class, InternalServerErrorException.class})
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
    }

}
