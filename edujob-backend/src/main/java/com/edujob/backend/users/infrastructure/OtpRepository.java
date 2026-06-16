package com.edujob.backend.users.infrastructure;

import com.edujob.backend.users.domain.OtpToken;
import com.edujob.backend.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<OtpToken, UUID> {
    
    // Busca un OTP concreto (ej: cuando el usuario escribe "123456" en la pantalla)
    Optional<OtpToken> findByOtp(String otp);
    
    // Busca si un usuario en concreto ya tiene un OTP generado (para no generarle 20 a la vez)
    Optional<OtpToken> findByUser(User user);
    
    // Borra todos los OTPs de un usuario (ej: una vez que lo ha usado con éxito)
    void deleteByUser(User user);
}