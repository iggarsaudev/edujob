package com.edujob.backend.users.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otp_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relación One-to-One: Cada código OTP pertenece a un solo usuario
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, length = 6)
    private String otp; // El código de 6 dígitos (ej: 481923)

    @Column(nullable = false)
    private LocalDateTime expiryDate; // Cuándo caduca el código (ej: en 15 min)

    // Un método útil para saber si ya ha pasado el tiempo
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}