package com.edujob.backend.config;

import java.util.List;

// Usamos <T> (Generics) para que este sobre sirva para List<JobOfferResponse>, List<CourseResponse>, etc.
public record PageResponse<T>(
        List<T> content,       // Los datos reales (ej: las 10 ofertas)
        int pageNumber,        // Página actual (empieza en 0)
        int pageSize,          // Tamaño de la página (ej: 10)
        long totalElements,    // Cuántos elementos hay en total en la base de datos
        int totalPages,        // Cuántas páginas hay en total
        boolean isLast         // ¿Es la última página? (Útil para que el Frontend oculte el botón "Siguiente")
) {}