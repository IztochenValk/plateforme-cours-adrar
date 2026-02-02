package com.adrar.skillforge;

import com.adrar.skillforge.entity.Role;
import com.adrar.skillforge.entity.User;
import com.adrar.skillforge.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        boolean hasAdmin = !userRepository.findAllByRole(Role.ADMIN).isEmpty();
        if (hasAdmin) return;

        if (userRepository.existsByUsername("admin") || userRepository.existsByEmail("admin@skillforge.local")) {
            return;
        }

        User admin = new User(
            "admin",
            "admin@skillforge.local",
            passwordEncoder.encode("admin12345")
        );
        admin.setRoles(Set.of(Role.ADMIN));

        userRepository.save(admin);

        System.out.println("Admin seed créé: username=admin password=admin12345");
    }
}
