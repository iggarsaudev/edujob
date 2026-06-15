package com.edujob.backend.users.infrastructure;

import com.edujob.backend.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Busca un usuario por DNI. Devuelve un Optional por si el usuario no existe.
    Optional<User> findByDni(String dni);
}