package com.edujob.backend.offers.application;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobOfferResponse(
        UUID id,
        String title,
        String description,
        String location,
        String salaryRange,
        String status,
        String authorName, // Solo enviamos el nombre, no el objeto User entero
        LocalDateTime createdAt
) {}