package com.edujob.backend.offers.application;

import com.edujob.backend.offers.domain.JobOffer;
import com.edujob.backend.offers.domain.OfferStatus;
import com.edujob.backend.offers.infrastructure.JobOfferRepository;
import com.edujob.backend.users.domain.User;
import com.edujob.backend.users.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.edujob.backend.config.PageResponse;

@Service
public class JobOfferService {

    private final JobOfferRepository offerRepository;
    private final UserRepository userRepository;

    public JobOfferService(JobOfferRepository offerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    // 1. Crear una oferta
    public JobOfferResponse createOffer(JobOfferRequest request, String authorDni) {
        User author = userRepository.findByDni(authorDni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        JobOffer newOffer = JobOffer.builder()
                .title(request.title())
                .description(request.description())
                .location(request.location())
                .salaryRange(request.salaryRange())
                .author(author)
                // status y createdAt se ponen solos por el @PrePersist
                .build();

        JobOffer savedOffer = offerRepository.save(newOffer);
        return mapToResponse(savedOffer);
    }

    // 2. Ver todas las ofertas ABIERTAS (Paginadas)
    public PageResponse<JobOfferResponse> getAllOpenOffers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> offersPage = offerRepository.findByStatusOrderByCreatedAtDesc(OfferStatus.OPEN, pageable);

        List<JobOfferResponse> content = offersPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList(); // Usamos .toList() nativo de Java 16+ en vez de Collectors

        return new PageResponse<>(
                content,
                offersPage.getNumber(),
                offersPage.getSize(),
                offersPage.getTotalElements(),
                offersPage.getTotalPages(),
                offersPage.isLast()
        );
    }

    // 3. Ver mis propias ofertas (Paginadas)
    public PageResponse<JobOfferResponse> getMyOffers(String authorDni, int page, int size) {
        User author = userRepository.findByDni(authorDni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> offersPage = offerRepository.findByAuthorIdOrderByCreatedAtDesc(author.getId(), pageable);

        List<JobOfferResponse> content = offersPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PageResponse<>(
                content,
                offersPage.getNumber(),
                offersPage.getSize(),
                offersPage.getTotalElements(),
                offersPage.getTotalPages(),
                offersPage.isLast()
        );
    }

    // 4. Cerrar una oferta (Ej: ya he contratado a alguien)
    public JobOfferResponse closeOffer(UUID offerId, String authorDni) {
        JobOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));

        // Seguridad extra: Solo el creador de la oferta puede cerrarla
        if (!offer.getAuthor().getDni().equals(authorDni)) {
            throw new RuntimeException("No tienes permiso para cerrar esta oferta");
        }

        offer.setStatus(OfferStatus.CLOSED);
        return mapToResponse(offerRepository.save(offer));
    }

    // Método auxiliar (privado) para convertir Entidad a DTO
    private JobOfferResponse mapToResponse(JobOffer offer) {
        return new JobOfferResponse(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getLocation(),
                offer.getSalaryRange(),
                offer.getStatus().name(),
                offer.getAuthor().getName() + " " + offer.getAuthor().getLastName(), // Concatenamos Nombre + Apellido
                offer.getCreatedAt()
        );
    }
}