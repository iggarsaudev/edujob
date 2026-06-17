package com.edujob.backend.offers.infrastructure;

import com.edujob.backend.offers.domain.JobOffer;
import com.edujob.backend.offers.domain.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, UUID> {
    
    // Spring crea automáticamente la query SQL: SELECT * FROM job_offers WHERE status = ? ORDER BY created_at DESC
    List<JobOffer> findByStatusOrderByCreatedAtDesc(OfferStatus status);

    // Spring crea: SELECT * FROM job_offers WHERE author_id = ? ORDER BY created_at DESC
    List<JobOffer> findByAuthorIdOrderByCreatedAtDesc(UUID authorId);
}