package com.edujob.backend.courses.application;

import java.util.UUID;

public record CourseDTO(UUID id, String title, String description, boolean isCheckedIn) {}