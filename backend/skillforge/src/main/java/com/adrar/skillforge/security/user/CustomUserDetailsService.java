package com.adrar.skillforge.security.user;

import com.adrar.skillforge.entity.User;
import com.adrar.skillforge.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(identifier)
            .orElseGet(() -> userRepository.findByUsername(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"))
            );

        return new CustomUserDetails(user);
    }
}
