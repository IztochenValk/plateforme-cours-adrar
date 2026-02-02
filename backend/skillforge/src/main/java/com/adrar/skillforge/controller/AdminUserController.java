package com.adrar.skillforge.controller;

import com.adrar.skillforge.dto.user.SetUserRolesRequest;
import com.adrar.skillforge.dto.user.UserResponse;
import com.adrar.skillforge.entity.User;
import com.adrar.skillforge.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{publicId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> setRoles(
        @PathVariable UUID publicId,
        @Valid @RequestBody SetUserRolesRequest request
    ) {
        userService.setRoles(publicId, request.roles());
        User updated = userService.findByPublicId(publicId);
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @PostMapping("/{publicId}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> addRole(
        @PathVariable UUID publicId,
        @PathVariable String role
    ) {
        User updated = userService.addRole(publicId, role);
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @DeleteMapping("/{publicId}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> removeRole(
        @PathVariable UUID publicId,
        @PathVariable String role
    ) {
        User updated = userService.removeRole(publicId, role);
        return ResponseEntity.ok(UserResponse.from(updated));
    }
}
