package com.edujob.backend.users.infrastructure;

import com.edujob.backend.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Spring Boot implementará automáticamente los métodos save(), findById(), findAll(), etc.
}