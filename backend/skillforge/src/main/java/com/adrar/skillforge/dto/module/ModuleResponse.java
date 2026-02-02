package com.adrar.skillforge.dto.module;

import com.adrar.skillforge.entity.Module;
import java.time.Instant;
import java.util.UUID;

public record ModuleResponse(
    UUID publicId,
    UUID coursePublicId,
    String title,
    Integer position,
    Instant createdAt
) {
    public static ModuleResponse from(Module module) {
        return new ModuleResponse(
            module.getPublicId(),
            module.getCourse().getPublicId(),
            module.getTitle(),
            module.getPosition(),
            module.getCreatedAt()
        );
    }
}
