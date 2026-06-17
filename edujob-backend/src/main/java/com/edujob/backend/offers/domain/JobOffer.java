package com.edujob.backend.offers.domain;

import com.edujob.backend.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private String location; // ej: "Madrid", "Remoto", "Valencia"

    private String salaryRange; // ej: "20.000€ - 25.000€"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    // ¡La Relación Mágica! Muchas ofertas pueden pertenecer a un solo creador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OfferStatus.OPEN; // Por defecto nacen abiertas
        }
    }
}