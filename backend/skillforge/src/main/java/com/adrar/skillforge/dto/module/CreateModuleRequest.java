package com.adrar.skillforge.dto.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateModuleRequest(
    @NotBlank(message = "Un titre est requis.")
    String title,

    @Positive(message = "La position doit être positive.")
    Integer position
) {
}
