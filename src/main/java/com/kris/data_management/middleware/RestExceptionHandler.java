package com.kris.data_management.middleware;

import com.kris.data_management.common.exception.InternalServerError;
import com.kris.data_management.physical.exception.InvalidSqlIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kris.data_management.common.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(InvalidSqlIdentifierException.class)
  public ResponseEntity<Object> handleInvalidSqlIdentifier(InvalidSqlIdentifierException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(InternalServerError.class)
  public ResponseEntity<Object> HandleInternalServerError(InternalServerError ex) {
    Map<String, String> errorDetails = new HashMap<>();

    errorDetails.put("message", ex.getMessage());
    errorDetails.put("description", ex.getDescription());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
  }
}
