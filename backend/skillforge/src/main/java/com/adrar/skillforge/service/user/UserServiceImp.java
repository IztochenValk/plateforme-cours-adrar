package com.adrar.skillforge.service.user;

import com.adrar.skillforge.dto.user.CreateUserRequest;
import com.adrar.skillforge.dto.user.PatchUserRequest;
import com.adrar.skillforge.dto.user.UpdateUserRequest;
import com.adrar.skillforge.entity.Role;
import com.adrar.skillforge.entity.User;
import com.adrar.skillforge.exception.BadRequestException;
import com.adrar.skillforge.exception.ConflictException;
import com.adrar.skillforge.exception.NotFoundException;
import com.adrar.skillforge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByPublicId(UUID publicId) {
        return userRepository.findByPublicId(publicId)
            .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + publicId));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Cet email est déjà utilisé");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Ce nom d'utilisateur est déjà utilisé");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(
            request.username(),
            request.email(),
            hashedPassword
        );

        return userRepository.save(user);
    }

    @Override
    public User update(UUID publicId, UpdateUserRequest request) {
        User user = findByPublicId(publicId);

        if (request.username() != null && !request.username().isBlank()
            && !request.username().equals(user.getUsername())
            && userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Ce nom d'utilisateur est déjà utilisé");
        }

        if (request.email() != null && !request.email().isBlank()
            && !request.email().equals(user.getEmail())
            && userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Cet email est déjà utilisé");
        }

        user.setUsername(request.username());
        user.setEmail(request.email());

        return userRepository.save(user);
    }

    @Override
    public User patch(UUID publicId, PatchUserRequest request) {
        User user = findByPublicId(publicId);

        if (request.username() != null && !request.username().isBlank()) {
            if (!request.username().equals(user.getUsername())
                && userRepository.existsByUsername(request.username())) {
                throw new ConflictException("Ce nom d'utilisateur est déjà utilisé");
            }
            user.setUsername(request.username());
        }

        if (request.email() != null && !request.email().isBlank()) {
            if (!request.email().equals(user.getEmail())
                && userRepository.existsByEmail(request.email())) {
                throw new ConflictException("Cet email est déjà utilisé");
            }
            user.setEmail(request.email());
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteByPublicId(UUID publicId) {
        if (!userRepository.existsByPublicId(publicId)) {
            throw new NotFoundException("Utilisateur introuvable: " + publicId);
        }
        userRepository.deleteByPublicId(publicId);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Set<Role> setRoles(UUID publicId, Set<String> roles) {
        User user = findByPublicId(publicId);

        Set<Role> parsed = roles.stream()
            .filter(r -> r != null && !r.isBlank())
            .map(r -> r.trim().toUpperCase(Locale.ROOT))
            .map(this::parseRoleOrThrow)
            .collect(Collectors.toSet());

        if (parsed.isEmpty()) {
            throw new BadRequestException("La liste des rôles ne peut pas être vide.");
        }

        user.setRoles(parsed);
        userRepository.save(user);

        return user.getRoles();
    }

    @Override
    public User addRole(UUID publicId, String role) {
        User user = findByPublicId(publicId);
        Role parsed = parseRoleOrThrow(role);

        user.addRole(parsed);
        return userRepository.save(user);
    }

    @Override
    public User removeRole(UUID publicId, String role) {
        User user = findByPublicId(publicId);
        Role parsed = parseRoleOrThrow(role);

        user.removeRole(parsed);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.addRole(Role.STUDENT);
        }

        return userRepository.save(user);
    }

    private Role parseRoleOrThrow(String role) {
        if (role == null || role.isBlank()) {
            throw new BadRequestException("Rôle invalide.");
        }
        try {
            return Role.valueOf(role.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Rôle inconnu: " + role + ". Valeurs: STUDENT, TEACHER, ADMIN");
        }
    }
}
