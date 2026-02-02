package com.adrar.skillforge.dto.course;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ModuleTreeResponse(
    UUID publicId,
    String title,
    Integer position,
    Instant createdAt,
    List<LessonTreeResponse> lessons
) {}
