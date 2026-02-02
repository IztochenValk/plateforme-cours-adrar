package com.adrar.skillforge.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record PatchUserRequest(
    @Size(min = 3, max = 50, message = "Votre identifiant doit être compris entre 3 et 50 caractères.")
    String username,

    @Email(message = "Le format de l'email n'est pas valide.")
    String email
) {
}
