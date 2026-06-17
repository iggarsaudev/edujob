package com.edujob.backend.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Atrapa todos los RuntimeException que hemos puesto en nuestro código
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        // Devolvemos un 400 Bad Request en lugar del temido 500
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 2. Paracaídas de emergencia: Atrapa CUALQUIER otro error imprevisto (NullPointers, fallos de base de datos...)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericExceptions(Exception ex) {
        
        // Imprimimos el error real en la consola del servidor para poder investigar
        ex.printStackTrace(); 
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Ocurrió un error inesperado en el servidor. Por favor, inténtalo más tarde.",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}