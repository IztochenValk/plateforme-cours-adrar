package com.adrar.skillforge.dto.module;

import jakarta.validation.constraints.Positive;

public record PatchModuleRequest(
    String title,

    @Positive(message = "La position doit être positive.")
    Integer position
) {}
