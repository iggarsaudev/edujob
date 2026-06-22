package com.edujob.backend.attendance.application;

import com.edujob.backend.attendance.domain.Attendance;
import com.edujob.backend.attendance.domain.AttendanceType;
import com.edujob.backend.attendance.infrastructure.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    // Devuelve TRUE si el alumno está dentro (número impar de fichajes)
    public boolean isStudentCheckedIn(String studentDni, UUID courseId) {
        long count = attendanceRepository.countByStudentDniAndCourseId(studentDni, courseId);
        return count % 2 != 0;
    }

    public Attendance registerAttendance(String studentDni, UUID courseId) {
        
        // Usamos el nuevo método del repositorio
        long count = attendanceRepository.countByStudentDniAndCourseId(studentDni, courseId);
        AttendanceType type = (count % 2 == 0) ? AttendanceType.ENTRY : AttendanceType.EXIT;

        Attendance attendance = Attendance.builder()
                .studentDni(studentDni)
                .courseId(courseId) // Guardamos el curso en la entidad
                .timestamp(LocalDateTime.now())
                .type(type)
                .message(type == AttendanceType.ENTRY ? "Entrada registrada" : "Salida registrada")
                .build();

        return attendanceRepository.save(attendance);
    }
}