package com.edujob.backend.users.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users") // 'user' es palabra reservada en postgresql
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 9)
    private String dni; // login principal del usuario

    @Column(nullable = false)
    private String pin; // el pin de acceso (se guardará hasheado)

    @Column(nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // se ejecuta automáticamente justo antes de insertar en base de datos
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}