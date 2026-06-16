package com.edujob.backend.users.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Cogemos el correo que pusiste en el .env para ponerlo en el remitente
    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("EduJob <" + senderEmail + ">");
            message.setTo(toEmail);
            message.setSubject("Tu Código de Recuperación (EduJob)");
            message.setText("Hola,\n\nHas solicitado recuperar tu PIN de acceso en EduJob.\n\n" +
                    "Tu código de verificación es: " + otpCode + "\n\n" +
                    "Este código caducará en 15 minutos.\nSi no has sido tú, ignora este mensaje.");

            mailSender.send(message);
            System.out.println("✅ [Email] Correo enviado con éxito a: " + toEmail);
            
        } catch (Exception e) {
            System.err.println("❌ [Email] Error enviando correo: " + e.getMessage());
            // No lanzamos la excepción para no romper la app si el correo falla en producción
        }
    }
}