package com.adrar.skillforge.controller;

import com.adrar.skillforge.dto.auth.AuthResponse;
import com.adrar.skillforge.dto.auth.LoginRequest;
import com.adrar.skillforge.dto.auth.RegisterRequest;
import com.adrar.skillforge.dto.user.UserResponse;
import com.adrar.skillforge.security.jwt.JwtService;
import com.adrar.skillforge.security.user.CustomUserDetails;
import com.adrar.skillforge.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        var created = userService.create(request.toCreateUserRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.identifier(), request.password())
        );

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        Set<String> roles = principal.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toSet());

        String token = jwtService.generateToken(principal.getUsername(), roles);

        AuthResponse response = new AuthResponse(
            token,
            "Bearer",
            principal.getUsername(),
            principal.getEmail(),
            roles
        );

        return ResponseEntity.ok(response);
    }
}
