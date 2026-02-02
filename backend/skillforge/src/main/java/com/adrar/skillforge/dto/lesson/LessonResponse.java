package com.adrar.skillforge.dto.lesson;

import com.adrar.skillforge.entity.Lesson;

import java.time.Instant;
import java.util.UUID;

public record LessonResponse(
    UUID publicId,
    UUID modulePublicId,
    String title,
    Integer position,
    String content,
    Instant createdAt
) {
    public static LessonResponse from(Lesson lesson) {
        return new LessonResponse(
            lesson.getPublicId(),
            lesson.getModule().getPublicId(),
            lesson.getTitle(),
            lesson.getPosition(),
            lesson.getContent(),
            lesson.getCreatedAt()
        );
    }
}
