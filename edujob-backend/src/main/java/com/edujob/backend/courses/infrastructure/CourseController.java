package com.edujob.backend.courses.infrastructure;

import com.edujob.backend.courses.application.CourseRequest;
import com.edujob.backend.courses.application.CourseResponse;
import com.edujob.backend.courses.application.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import com.edujob.backend.config.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Cursos", description = "Endpoints para la gestión y matriculación de cursos")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Operation(summary = "Crear curso", description = "Crea un curso nuevo. El usuario autenticado debe tener rol TEACHER o ADMIN.")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request,
            Authentication authentication
    ) {
        CourseResponse response = courseService.createCourse(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos los cursos", description = "Devuelve el catálogo completo de cursos disponibles (paginado).")
    public ResponseEntity<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @GetMapping("/me")
    @Operation(summary = "Mis cursos", description = "Cursos que impartes o donde estás matriculado (paginado).")
    public ResponseEntity<PageResponse<CourseResponse>> getMyCourses(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(courseService.getMyCourses(authentication.getName(), page, size));
    }

    @PostMapping("/{id}/enroll")
    @Operation(summary = "Matricularse", description = "Matricula al usuario autenticado (debe ser STUDENT) en el curso especificado.")
    public ResponseEntity<CourseResponse> enroll(@PathVariable UUID id, Authentication authentication) {
        CourseResponse response = courseService.enrollStudent(id, authentication.getName());
        return ResponseEntity.ok(response);
    }
}