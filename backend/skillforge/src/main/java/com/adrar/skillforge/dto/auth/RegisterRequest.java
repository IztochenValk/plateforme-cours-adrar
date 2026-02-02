package com.adrar.skillforge.dto.auth;

import com.adrar.skillforge.dto.user.CreateUserRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Un identifiant est requis.")
    @Size(min = 3, max = 50, message = "Votre identifiant doit être compris entre 3 et 50 caractères.")
    String username,

    @NotBlank(message = "Un email est requis.")
    @Email(message = "Votre email doit avoir un format valide.")
    String email,

    @NotBlank(message = "Un mot de passe est requis")
    @Size(min = 8, message = "Votre mot de passe doit contenir au moins 8 caractères")
    String password
) {
    public CreateUserRequest toCreateUserRequest() {
        return new CreateUserRequest(username, email, password);
    }
}
