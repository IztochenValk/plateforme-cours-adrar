package com.adrar.skillforge.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Identifiant requis.")
    String identifier,

    @NotBlank(message = "Mot de passe requis.")
    String password
) {}
