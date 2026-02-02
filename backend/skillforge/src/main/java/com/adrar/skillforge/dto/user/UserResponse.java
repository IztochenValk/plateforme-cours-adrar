package com.adrar.skillforge.dto.user;

import com.adrar.skillforge.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponse(
    UUID publicId,
    String username,
    String email,
    Instant createdAt,
    Set<String> roles
) {
    public static UserResponse from(User user) {
        Set<String> roles = user.getRoles() == null
            ? Set.of()
            : user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new UserResponse(
            user.getPublicId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt(),
            roles
        );
    }
}
