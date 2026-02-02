package com.adrar.skillforge.dto.course;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CourseTreeResponse(
    UUID publicId,
    String title,
    String description,
    Instant createdAt,
    List<ModuleTreeResponse> modules
) {}
