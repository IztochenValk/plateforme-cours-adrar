package com.adrar.skillforge.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
    @NotBlank(message = "Un titre est requis.")
    @Size(min = 3, max = 200, message = "Le titre doit être compris entre 3 et 200 caractères.")
    String title,

    @Size(max = 4000, message = "La description ne doit pas dépasser 4000 caractères.")
    String description
) {
}
