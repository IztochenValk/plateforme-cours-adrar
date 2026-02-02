package com.adrar.skillforge.controller;

import com.adrar.skillforge.dto.user.CreateUserRequest;
import com.adrar.skillforge.dto.user.PatchUserRequest;
import com.adrar.skillforge.dto.user.UpdateUserRequest;
import com.adrar.skillforge.dto.user.UserResponse;
import com.adrar.skillforge.entity.User;
import com.adrar.skillforge.service.user.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll()
            .stream()
            .map(UserResponse::from)
            .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<UserResponse> findByPublicId(@PathVariable UUID publicId) {
        User user = userService.findByPublicId(publicId);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        User created = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<UserResponse> update(
        @PathVariable UUID publicId,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        User updated = userService.update(publicId, request);
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @PatchMapping("/{publicId}")
    public ResponseEntity<UserResponse> patch(
        @PathVariable UUID publicId,
        @Valid @RequestBody PatchUserRequest request
    ) {
        User updated = userService.patch(publicId, request);
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        userService.deleteByPublicId(publicId);
        return ResponseEntity.noContent().build();
    }
}
