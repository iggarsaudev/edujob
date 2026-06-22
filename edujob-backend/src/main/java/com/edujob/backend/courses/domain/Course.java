package com.edujob.backend.courses.domain;

import com.edujob.backend.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    // Relación MUCHOS a UNO (Muchos cursos los puede impartir un solo profesor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnore
    private User teacher;

    // Relación MUCHOS a MUCHOS (Un curso tiene muchos alumnos, un alumno tiene muchos cursos)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_enrollments", // Nombre de la tabla "puente" que Spring creará
            joinColumns = @JoinColumn(name = "course_id"), // Columna para el Curso
            inverseJoinColumns = @JoinColumn(name = "student_id") // Columna para el Alumno
    )
    @Builder.Default // Usamos esto para que Lombok inicialice el Set vacío correctamente
    @JsonIgnore
    private Set<User> enrolledStudents = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Método "Helper" (buena práctica) para matricular a un alumno de forma segura
    public void addStudent(User student) {
        this.enrolledStudents.add(student);
    }
}