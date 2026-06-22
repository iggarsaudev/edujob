package com.edujob.backend.attendance.infrastructure;

import com.edujob.backend.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    
    // Cuenta por alumno Y por curso
    long countByStudentDniAndCourseId(String studentDni, UUID courseId);
    
}