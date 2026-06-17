package com.edujob.backend.offers.infrastructure;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@Tag(name = "Ofertas de Empleo", description = "Endpoints para gestionar las ofertas de trabajo")
@SecurityRequirement(name = "bearerAuth") // Exige el candado de Swagger
public class JobOfferController {

    private final JobOfferService offerService;

    public JobOfferController(JobOfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    @Operation(summary = "Crear una oferta", description = "Crea una nueva oferta de trabajo a nombre del usuario autenticado.")
    public ResponseEntity<JobOfferResponse> createOffer(@RequestBody JobOfferRequest request, Authentication authentication) {
        // authentication.getName() saca el DNI del Token JWT automáticamente
        JobOfferResponse response = offerService.createOffer(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar ofertas abiertas", description = "Devuelve todas las ofertas con estado OPEN ordenadas por la más reciente.")
    public ResponseEntity<List<JobOfferResponse>> getAllOpenOffers() {
        return ResponseEntity.ok(offerService.getAllOpenOffers());
    }

    @GetMapping("/me")
    @Operation(summary = "Mis ofertas", description = "Devuelve todas las ofertas (abiertas y cerradas) creadas por el usuario autenticado.")
    public ResponseEntity<List<JobOfferResponse>> getMyOffers(Authentication authentication) {
        return ResponseEntity.ok(offerService.getMyOffers(authentication.getName()));
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Cerrar una oferta", description = "Cambia el estado de una oferta a CLOSED. Solo puede hacerlo el autor.")
    public ResponseEntity<JobOfferResponse> closeOffer(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(offerService.closeOffer(id, authentication.getName()));
    }
}