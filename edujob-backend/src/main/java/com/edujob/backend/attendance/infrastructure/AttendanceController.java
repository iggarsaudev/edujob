package com.edujob.backend.attendance.infrastructure;

import com.edujob.backend.attendance.application.AttendanceService;
import com.edujob.backend.attendance.domain.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor // Nos inyecta el servicio automáticamente
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/{courseId}/clock-in")
    public ResponseEntity<Attendance> clockIn(@PathVariable UUID courseId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String studentDni = authentication.getName();

        // Le pasamos el courseId al servicio
        Attendance savedAttendance = attendanceService.registerAttendance(studentDni, courseId);

        return ResponseEntity.ok(savedAttendance);
    }
}