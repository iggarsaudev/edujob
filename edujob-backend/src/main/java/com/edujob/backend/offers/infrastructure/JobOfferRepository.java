package com.edujob.backend.offers.infrastructure;

import com.edujob.backend.offers.domain.JobOffer;
import com.edujob.backend.offers.domain.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, UUID> {
    
    // Al pasarle 'Pageable', Spring añade automáticamente un "LIMIT" y "OFFSET" a la consulta SQL
    Page<JobOffer> findByStatusOrderByCreatedAtDesc(OfferStatus status, Pageable pageable);

    Page<JobOffer> findByAuthorIdOrderByCreatedAtDesc(UUID authorId, Pageable pageable);
}