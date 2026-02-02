package com.adrar.skillforge.dto.course;

import com.adrar.skillforge.entity.Course;

import java.time.Instant;
import java.util.UUID;

public record CourseResponse(
    UUID publicId,
    String title,
    String description,
    Instant createdAt
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
            course.getPublicId(),
            course.getTitle(),
            course.getDescription(),
            course.getCreatedAt()
        );
    }
}
