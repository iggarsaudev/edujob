package com.edujob.backend.courses.application;

import com.edujob.backend.courses.domain.Course;
import com.edujob.backend.courses.infrastructure.CourseRepository;
import com.edujob.backend.users.domain.Role;
import com.edujob.backend.users.domain.User;
import com.edujob.backend.users.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // 1. Crear un curso (Solo Profesores)
    public CourseResponse createCourse(CourseRequest request, String teacherDni) {
        User teacher = userRepository.findByDni(teacherDni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (teacher.getRole() != Role.TEACHER && teacher.getRole() != Role.ADMIN) {
            throw new RuntimeException("Solo los docentes pueden crear cursos");
        }

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .teacher(teacher)
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    // 2. Ver todos los cursos
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 3. Ver MIS cursos (Lógica doble: si soy profe enseño los que dicto, si soy alumno los que estudio)
    public List<CourseResponse> getMyCourses(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Course> courses;
        if (user.getRole() == Role.TEACHER) {
            courses = courseRepository.findByTeacherIdOrderByCreatedAtDesc(user.getId());
        } else {
            courses = courseRepository.findByEnrolledStudents_IdOrderByCreatedAtDesc(user.getId());
        }

        return courses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // 4. Matricular a un alumno
    @Transactional
    public CourseResponse enrollStudent(UUID courseId, String studentDni) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        User student = userRepository.findByDni(studentDni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Solo los alumnos pueden matricularse en los cursos");
        }

        // Añadimos al alumno usando el método Helper de nuestra Entidad
        course.addStudent(student);
        
        // Guardamos (Hibernate insertará la fila en la tabla puente automáticamente)
        return mapToResponse(courseRepository.save(course));
    }

    // Método privado auxiliar para mapear Entidad a DTO
    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getTeacher().getName() + " " + course.getTeacher().getLastName(),
                course.getEnrolledStudents().size(), // Contamos cuántos alumnos hay
                course.getCreatedAt()
        );
    }
}