package com.adrar.skillforge.repository;

import com.adrar.skillforge.entity.Role;
import com.adrar.skillforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPublicId(UUID publicId);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPublicId(UUID publicId);

    void deleteByPublicId(UUID publicId);

    @Query("select distinct u from User u join u.roles r where r = :role")
    List<User> findAllByRole(@Param("role") Role role);
}
