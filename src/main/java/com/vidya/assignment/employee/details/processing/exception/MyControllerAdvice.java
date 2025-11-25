package com.vidya.assignment.employee.details.processing.exception;

import com.vidya.assignment.employee.details.processing.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Error> handleException(Throwable ex) {

        log.error("Exception occurred", ex);

        return new ResponseEntity<>(Error.builder()
                .code("ERROR_001")
                .message("Internal Server Error")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Error> handleConstraintViolationException(ServiceException ex) {

        log.error("Exception occurred", ex);

        return new ResponseEntity<>(
                Error.builder()
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getErrorCode().getMessage())
                        .build()
                , HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
