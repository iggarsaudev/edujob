package com.edujob.backend.courses.infrastructure;

import com.edujob.backend.courses.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    
    // Buscar todos los cursos que imparte un profesor concreto
    List<Course> findByTeacherIdOrderByCreatedAtDesc(UUID teacherId);
    
    // Magia pura de Spring Data: Buscar todos los cursos en los que un estudiante concreto esté dentro de la lista 'enrolledStudents'
    List<Course> findByEnrolledStudents_IdOrderByCreatedAtDesc(UUID studentId);
}