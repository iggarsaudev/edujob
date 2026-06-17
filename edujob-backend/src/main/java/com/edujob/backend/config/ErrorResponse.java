package com.edujob.backend.config;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,   // El mensaje para el usuario (ej: "Curso no encontrado")
        String error,     // El tipo de error técnico (ej: "Bad Request")
        int status,       // El código HTTP (ej: 400)
        LocalDateTime timestamp // Cuándo ocurrió
) {}