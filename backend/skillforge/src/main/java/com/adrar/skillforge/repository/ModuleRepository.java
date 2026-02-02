package com.adrar.skillforge.repository;

import com.adrar.skillforge.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByPublicId(UUID publicId);
    boolean existsByPublicId(UUID publicId);
    void deleteByPublicId(UUID publicId);

    List<Module> findAllByCourse_PublicIdOrderByPositionAsc(UUID coursePublicId);
}
