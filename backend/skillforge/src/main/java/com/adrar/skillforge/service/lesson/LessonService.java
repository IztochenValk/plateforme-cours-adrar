package com.adrar.skillforge.service.lesson;

import com.adrar.skillforge.dto.lesson.CreateLessonRequest;
import com.adrar.skillforge.dto.lesson.PatchLessonRequest;
import com.adrar.skillforge.entity.Lesson;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    Lesson findByPublicId(UUID publicId);

    List<Lesson> findAllByModule(UUID modulePublicId);

    Lesson create(UUID modulePublicId, CreateLessonRequest request);

    Lesson patch(UUID lessonPublicId, PatchLessonRequest request);

    void delete(UUID lessonPublicId);
}
