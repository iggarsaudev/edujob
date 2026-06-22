package com.edujob.backend.courses.infrastructure;

import com.edujob.backend.courses.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    
    Page<Course> findByTeacherIdOrderByCreatedAtDesc(UUID teacherId, Pageable pageable);
    
    Page<Course> findByEnrolledStudents_IdOrderByCreatedAtDesc(UUID studentId, Pageable pageable);

    // Spring crea la consulta SQL cruzando la tabla de cursos y la de usuarios mediante el DNI
    List<Course> findByEnrolledStudentsDni(String dni);
}