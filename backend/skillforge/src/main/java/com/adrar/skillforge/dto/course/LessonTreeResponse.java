package com.adrar.skillforge.dto.course;

import java.time.Instant;
import java.util.UUID;

public record LessonTreeResponse(
    UUID publicId,
    String title,
    Integer position,
    String content,
    Instant createdAt
) {}
