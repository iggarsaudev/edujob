package com.edujob.backend.users.infrastructure;

import com.edujob.backend.users.domain.Role;
import com.edujob.backend.users.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // Le dice a Spring que gestione esta clase y la ejecute al arrancar
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    // Inyección de dependencias
    public UserDataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Solo insertamos si la tabla está vacía
        if (userRepository.count() == 0) {
            
            User admin = User.builder()
                    .dni("11111111A")
                    .pin("1234")
                    .name("Admin")
                    .lastName("EduJob")
                    .email("admin@edujob.com")
                    .role(Role.ADMIN)
                    .build();

            User teacher = User.builder()
                    .dni("22222222B")
                    .pin("1234")
                    .name("Laura")
                    .lastName("García")
                    .email("laura.docente@edujob.com")
                    .role(Role.TEACHER)
                    .build();

            User student = User.builder()
                    .dni("33333333C")
                    .pin("1234")
                    .name("Carlos")
                    .lastName("Martínez")
                    .email("carlos.alumno@edujob.com")
                    .role(Role.STUDENT)
                    .build();

            // Guardamos los 3 de golpe en PostgreSQL
            userRepository.saveAll(List.of(admin, teacher, student));
            
            System.out.println("✅ [EduJob] Data Seeding: Usuarios de prueba insertados correctamente.");
        }
    }
}