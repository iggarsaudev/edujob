package com.edujob.backend.courses.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseRequest(
        @NotBlank(message = "El título del curso no puede estar vacío")
        @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description
) {}