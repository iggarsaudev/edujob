package com.edujob.backend.offers.application;

public record JobOfferRequest(
        String title,
        String description,
        String location,
        String salaryRange
) {}