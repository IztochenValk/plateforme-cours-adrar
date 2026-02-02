package com.adrar.skillforge.controller;

import com.adrar.skillforge.dto.lesson.CreateLessonRequest;
import com.adrar.skillforge.dto.lesson.LessonResponse;
import com.adrar.skillforge.dto.lesson.PatchLessonRequest;
import com.adrar.skillforge.entity.Lesson;
import com.adrar.skillforge.service.lesson.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/modules/{modulePublicId}/lessons")
    public ResponseEntity<List<LessonResponse>> findAllByModule(
        @PathVariable UUID modulePublicId
    ) {
        List<LessonResponse> lessons = lessonService.findAllByModule(modulePublicId)
            .stream()
            .map(LessonResponse::from)
            .toList();

        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/modules/{modulePublicId}/lessons")
    public ResponseEntity<LessonResponse> create(
        @PathVariable UUID modulePublicId,
        @Valid @RequestBody CreateLessonRequest request
    ) {
        Lesson created = lessonService.create(modulePublicId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(LessonResponse.from(created));
    }

    @PatchMapping("/lessons/{lessonPublicId}")
    public ResponseEntity<LessonResponse> patch(
        @PathVariable UUID lessonPublicId,
        @Valid @RequestBody PatchLessonRequest request
    ) {
        Lesson updated = lessonService.patch(lessonPublicId, request);
        return ResponseEntity.ok(LessonResponse.from(updated));
    }

    @DeleteMapping("/lessons/{lessonPublicId}")
    public ResponseEntity<Void> delete(@PathVariable UUID lessonPublicId) {
        lessonService.delete(lessonPublicId);
        return ResponseEntity.noContent().build();
    }
}
