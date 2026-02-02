package com.adrar.skillforge.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateLessonRequest(
    @NotBlank(message = "Un titre est requis.")
    String title,

    @Positive(message = "La position doit être positive.")
    Integer position,

    String content
) {
}
