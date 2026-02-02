package com.adrar.skillforge.repository;

import com.adrar.skillforge.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByPublicId(UUID publicId);

    Optional<Course> findByPublicId(UUID publicId);

    void deleteByPublicId(UUID publicId);

    @Query("""
        select distinct c
        from Course c
        left join fetch c.modules m
        left join fetch m.lessons l
        where c.publicId = :publicId
        """)
    Optional<Course> findTreeByPublicId(@Param("publicId") UUID publicId);

    @Query("""
        select distinct c
        from Course c
        left join fetch c.modules m
        left join fetch m.lessons l
        """)
    List<Course> findAllTrees();

    @Query("""
        select distinct c
        from Course c
        left join fetch c.modules m
        left join fetch m.lessons l
        where c.publicId in :publicIds
        """)
    List<Course> findTreesByPublicIds(@Param("publicIds") List<UUID> publicIds);
}
