package com.edujob.backend.users.application;

import com.edujob.backend.users.domain.OtpToken;
import com.edujob.backend.users.domain.User;
import com.edujob.backend.users.infrastructure.OtpRepository;
import com.edujob.backend.users.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // Leemos nuestra variable mágica del .env
    @Value("${app.portfolio.demo-mode}")
    private boolean isDemoMode;

    public OtpService(OtpRepository otpRepository, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public OtpResponse generateAndSendOtp(String dni) {
        // 1. Buscamos si el usuario existe
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("No existe ningún usuario con ese DNI"));

        // 2. Limpiamos códigos antiguos si tuviera alguno
        otpRepository.deleteByUser(user);

        // 3. Generamos un código aleatorio de 6 dígitos
        String generatedOtp = String.format("%06d", new Random().nextInt(999999));

        // 4. Lo guardamos en la base de datos (caduca en 15 minutos)
        OtpToken otpToken = OtpToken.builder()
                .user(user)
                .otp(generatedOtp)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        otpRepository.save(otpToken);

        // 5. Intentamos enviar el correo (Brevo)
        emailService.sendOtpEmail(user.getEmail(), generatedOtp);

        // 6. Lógica del MODO PORTFOLIO
        if (isDemoMode) {
            System.out.println("⚠️ [MODO PORTFOLIO ACTIVO] Código OTP generado: " + generatedOtp);
            return new OtpResponse("Modo Demo: Código adjunto en la respuesta.", generatedOtp);
        }

        // Si es producción real, devolvemos null en el código para que el frontend no lo sepa
        return new OtpResponse("Se ha enviado un código a tu correo electrónico.", null);
    }

    @Transactional
    public void resetPin(String otpCode, String newPin) {
        // 1. Buscamos el código en la base de datos
        OtpToken otpToken = otpRepository.findByOtp(otpCode)
                .orElseThrow(() -> new RuntimeException("El código introducido no es válido"));

        // 2. Comprobamos si ha caducado
        if (otpToken.isExpired()) {
            otpRepository.delete(otpToken); // Lo borramos por seguridad
            throw new RuntimeException("El código ha caducado. Solicita uno nuevo.");
        }

        // 3. Actualizamos el PIN del usuario (¡encriptado!)
        User user = otpToken.getUser();
        user.setPin(passwordEncoder.encode(newPin));
        userRepository.save(user);

        // 4. Borramos el código usado para que no se pueda reutilizar
        otpRepository.delete(otpToken);
    }
}