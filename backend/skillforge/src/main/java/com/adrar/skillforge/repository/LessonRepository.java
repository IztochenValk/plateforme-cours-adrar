package com.adrar.skillforge.repository;

import com.adrar.skillforge.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByPublicId(UUID publicId);
    boolean existsByPublicId(UUID publicId);
    void deleteByPublicId(UUID publicId);

    List<Lesson> findAllByModule_PublicIdOrderByPositionAsc(UUID modulePublicId);
}
