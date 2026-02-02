package com.adrar.skillforge.dto.lesson;

import jakarta.validation.constraints.Positive;

public record PatchLessonRequest(
    String title,

    @Positive(message = "La position doit être positive.")
    Integer position,

    String content
) {
}
