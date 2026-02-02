package com.adrar.skillforge.dto.user;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record SetUserRolesRequest(
    @NotEmpty(message = "La liste des rôles ne peut pas être vide.")
    Set<String> roles
) {}
