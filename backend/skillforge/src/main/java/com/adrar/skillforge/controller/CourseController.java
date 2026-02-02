package com.adrar.skillforge.controller;

import com.adrar.skillforge.api.PaginationRequest;
import com.adrar.skillforge.dto.course.CourseResponse;
import com.adrar.skillforge.dto.course.CourseTreeResponse;
import com.adrar.skillforge.dto.course.CreateCourseRequest;
import com.adrar.skillforge.dto.course.PatchCourseRequest;
import com.adrar.skillforge.dto.course.UpdateCourseRequest;
import com.adrar.skillforge.entity.Course;
import com.adrar.skillforge.service.course.CourseService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> findAll(Pageable pageable) {
        Pageable safe = PaginationRequest.enforce(pageable);

        Page<CourseResponse> page = courseService.findAll(safe)
            .map(CourseResponse::from);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/tree")
    public ResponseEntity<Page<CourseTreeResponse>> getTrees(Pageable pageable) {
        Pageable safe = PaginationRequest.enforce(pageable);
        return ResponseEntity.ok(courseService.getTrees(safe));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<CourseResponse> findByPublicId(@PathVariable UUID publicId) {
        Course course = courseService.findByPublicId(publicId);
        return ResponseEntity.ok(CourseResponse.from(course));
    }

    @GetMapping("/{publicId}/tree")
    public ResponseEntity<CourseTreeResponse> getTree(@PathVariable UUID publicId) {
        return ResponseEntity.ok(courseService.getTree(publicId));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CreateCourseRequest request) {
        Course created = courseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseResponse.from(created));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<CourseResponse> update(
        @PathVariable UUID publicId,
        @Valid @RequestBody UpdateCourseRequest request
    ) {
        Course updated = courseService.update(publicId, request);
        return ResponseEntity.ok(CourseResponse.from(updated));
    }

    @PatchMapping("/{publicId}")
    public ResponseEntity<CourseResponse> patch(
        @PathVariable UUID publicId,
        @Valid @RequestBody PatchCourseRequest request
    ) {
        Course updated = courseService.patch(publicId, request);
        return ResponseEntity.ok(CourseResponse.from(updated));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        courseService.deleteByPublicId(publicId);
        return ResponseEntity.noContent().build();
    }
}
