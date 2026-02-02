package com.adrar.skillforge.dto.auth;

import java.util.Set;

public record AuthResponse(
    String token,
    String tokenType,
    String username,
    String email,
    Set<String> roles
) {}
