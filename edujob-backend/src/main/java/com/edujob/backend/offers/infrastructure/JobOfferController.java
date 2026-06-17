package com.edujob.backend.offers.infrastructure;

import com.edujob.backend.config.PageResponse;
import com.edujob.backend.offers.application.JobOfferRequest;
import com.edujob.backend.offers.application.JobOfferResponse;
import com.edujob.backend.offers.application.JobOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@Tag(name = "Ofertas de Empleo", description = "Endpoints para gestionar las ofertas de trabajo")
@SecurityRequirement(name = "bearerAuth")
public class JobOfferController {

    private final JobOfferService offerService;

    public JobOfferController(JobOfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    @Operation(summary = "Crear una oferta", description = "Crea una nueva oferta de trabajo a nombre del usuario autenticado.")
    public ResponseEntity<JobOfferResponse> createOffer(@RequestBody JobOfferRequest request, Authentication authentication) {
        JobOfferResponse response = offerService.createOffer(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar ofertas abiertas", description = "Devuelve las ofertas paginadas (por defecto 10 por página).")
    public ResponseEntity<PageResponse<JobOfferResponse>> getAllOpenOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(offerService.getAllOpenOffers(page, size));
    }

    @GetMapping("/me")
    @Operation(summary = "Mis ofertas", description = "Devuelve mis ofertas paginadas.")
    public ResponseEntity<PageResponse<JobOfferResponse>> getMyOffers(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(offerService.getMyOffers(authentication.getName(), page, size));
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Cerrar una oferta", description = "Cambia el estado de una oferta a CLOSED. Solo puede hacerlo el autor.")
    public ResponseEntity<JobOfferResponse> closeOffer(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(offerService.closeOffer(id, authentication.getName()));
    }
}