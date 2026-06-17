package com.edujob.backend.courses.application;

import com.edujob.backend.courses.domain.Course;
import com.edujob.backend.courses.infrastructure.CourseRepository;
import com.edujob.backend.users.domain.Role;
import com.edujob.backend.users.domain.User;
import com.edujob.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Le decimos a JUnit que vamos a usar dobles de acción (Mocks)
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    // 1. MOCKS: Simulamos la base de datos para no tocar datos reales
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    // 2. INJECT MOCKS: Instanciamos el servicio inyectándole los Mocks por debajo
    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_Success_WhenUserIsTeacher() {
        // --- ARRANGE (Preparar el escenario) ---
        String teacherDni = "22222222B";
        CourseRequest request = new CourseRequest("Java Avanzado", "Curso de Spring Boot");

        // Creamos un usuario falso con rol TEACHER
        User mockTeacher = new User();
        mockTeacher.setId(UUID.randomUUID());
        mockTeacher.setDni(teacherDni);
        mockTeacher.setName("Laura");
        mockTeacher.setLastName("García");
        mockTeacher.setRole(Role.TEACHER);

        // Creamos un curso falso como si lo devolviera la base de datos
        Course mockSavedCourse = Course.builder()
                .id(UUID.randomUUID())
                .title(request.title())
                .description(request.description())
                .teacher(mockTeacher)
                .createdAt(LocalDateTime.now())
                .build();

        // Le dictamos el guion a los Mocks: "Cuando te llamen, responde esto"
        when(userRepository.findByDni(teacherDni)).thenReturn(Optional.of(mockTeacher));
        when(courseRepository.save(any(Course.class))).thenReturn(mockSavedCourse);

        // --- ACT (Actuar) ---
        CourseResponse response = courseService.createCourse(request, teacherDni);

        // --- ASSERT (Comprobar) ---
        assertNotNull(response);
        assertEquals("Java Avanzado", response.title());
        assertEquals("Laura García", response.teacherName());
        
        // Verificamos que el repositorio intentó guardar en base de datos exactamente 1 vez
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createCourse_ThrowsException_WhenUserIsStudent() {
        // --- ARRANGE ---
        String studentDni = "33333333C";
        CourseRequest request = new CourseRequest("Hackear la NASA", "Intento de curso");

        // Creamos un usuario falso pero con rol STUDENT
        User mockStudent = new User();
        mockStudent.setDni(studentDni);
        mockStudent.setRole(Role.STUDENT);

        when(userRepository.findByDni(studentDni)).thenReturn(Optional.of(mockStudent));

        // --- ACT & ASSERT ---
        // Comprobamos que al intentar crear el curso, el servicio lanza una excepción en seco
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.createCourse(request, studentDni);
        });

        assertEquals("Solo los docentes pueden crear cursos", exception.getMessage());
        
        // Verificamos que el repositorio NUNCA guardó nada por seguridad
        verify(courseRepository, never()).save(any(Course.class));
    }
}