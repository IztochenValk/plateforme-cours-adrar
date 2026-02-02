package com.adrar.skillforge.service.lesson;

import com.adrar.skillforge.dto.lesson.CreateLessonRequest;
import com.adrar.skillforge.dto.lesson.PatchLessonRequest;
import com.adrar.skillforge.entity.Lesson;
import com.adrar.skillforge.entity.Module;
import com.adrar.skillforge.exception.NotFoundException;
import com.adrar.skillforge.repository.LessonRepository;
import com.adrar.skillforge.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LessonServiceImp implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    public LessonServiceImp(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Lesson findByPublicId(UUID publicId) {
        return lessonRepository.findByPublicId(publicId)
            .orElseThrow(() -> new NotFoundException("Leçon introuvable: " + publicId));
    }

    @Override
    public List<Lesson> findAllByModule(UUID modulePublicId) {
        return lessonRepository.findAllByModule_PublicIdOrderByPositionAsc(modulePublicId);
    }

    @Override
    public Lesson create(UUID modulePublicId, CreateLessonRequest request) {
        Module module = moduleRepository.findByPublicId(modulePublicId)
            .orElseThrow(() -> new NotFoundException("Module introuvable: " + modulePublicId));

        Integer position = request.position();
        if (position == null) {
            position = module.getLessons().size() + 1;
        }

        Lesson lesson = new Lesson(request.title(), position, request.content());
        lesson.setModule(module);

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson patch(UUID lessonPublicId, PatchLessonRequest request) {
        Lesson lesson = findByPublicId(lessonPublicId);

        if (request.title() != null && !request.title().isBlank()) {
            lesson.setTitle(request.title());
        }

        if (request.position() != null) {
            lesson.setPosition(request.position());
        }

        if (request.content() != null) {
            lesson.setContent(request.content());
        }

        return lessonRepository.save(lesson);
    }

    @Override
    public void delete(UUID lessonPublicId) {
        if (!lessonRepository.existsByPublicId(lessonPublicId)) {
            throw new NotFoundException("Leçon introuvable: " + lessonPublicId);
        }
        lessonRepository.deleteByPublicId(lessonPublicId);
    }
}
