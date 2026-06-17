package com.edujob.backend.courses.application;

import java.time.LocalDateTime;
import java.util.UUID;

public record CourseResponse(
        UUID id,
        String title,
        String description,
        String teacherName,
        int enrolledStudentsCount, // Solo mandamos el contador
        LocalDateTime createdAt
) {}