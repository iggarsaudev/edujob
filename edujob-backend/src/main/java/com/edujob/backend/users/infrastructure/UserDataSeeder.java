package com.edujob.backend.users.infrastructure;

import com.edujob.backend.users.domain.Role;
import com.edujob.backend.users.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    
    public UserDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            
            User admin = User.builder()
                    .dni("11111111A")
                    .pin(passwordEncoder.encode("1234")) // ENCRIPTAMOS AQUÍ
                    .name("Admin")
                    .lastName("EduJob")
                    .email("admin@edujob.com")
                    .role(Role.ADMIN)
                    .build();

            User teacher = User.builder()
                    .dni("22222222B")
                    .pin(passwordEncoder.encode("1234")) // ENCRIPTAMOS AQUÍ
                    .name("Laura")
                    .lastName("García")
                    .email("laura.docente@edujob.com")
                    .role(Role.TEACHER)
                    .build();

            User student = User.builder()
                    .dni("33333333C")
                    .pin(passwordEncoder.encode("1234")) // ENCRIPTAMOS AQUÍ
                    .name("Carlos")
                    .lastName("Martínez")
                    .email("carlos.alumno@edujob.com")
                    .role(Role.STUDENT)
                    .build();

            userRepository.saveAll(List.of(admin, teacher, student));
            
            System.out.println("✅ [EduJob] Data Seeding: Usuarios de prueba insertados con PINs encriptados.");
        }
    }
}