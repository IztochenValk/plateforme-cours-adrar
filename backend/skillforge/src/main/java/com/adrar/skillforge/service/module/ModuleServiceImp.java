package com.adrar.skillforge.service.module;

import com.adrar.skillforge.dto.module.CreateModuleRequest;
import com.adrar.skillforge.dto.module.PatchModuleRequest;
import com.adrar.skillforge.entity.Course;
import com.adrar.skillforge.entity.Module;
import com.adrar.skillforge.exception.NotFoundException;
import com.adrar.skillforge.repository.CourseRepository;
import com.adrar.skillforge.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImp implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    public ModuleServiceImp(ModuleRepository moduleRepository, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Module findByPublicId(UUID publicId) {
        return moduleRepository.findByPublicId(publicId)
            .orElseThrow(() -> new NotFoundException("Module introuvable: " + publicId));
    }

    @Override
    public List<Module> findAllByCourse(UUID coursePublicId) {
        return moduleRepository.findAllByCourse_PublicIdOrderByPositionAsc(coursePublicId);
    }

    @Override
    public Module create(UUID coursePublicId, CreateModuleRequest request) {
        Course course = courseRepository.findByPublicId(coursePublicId)
            .orElseThrow(() -> new NotFoundException("Cours introuvable: " + coursePublicId));

        Integer position = request.position();
        if (position == null) {
            position = course.getModules().size() + 1;
        }

        Module module = new Module(request.title(), position);
        module.setCourse(course);

        return moduleRepository.save(module);
    }

    @Override
    public Module patch(UUID modulePublicId, PatchModuleRequest request) {
        Module module = findByPublicId(modulePublicId);

        if (request.title() != null && !request.title().isBlank()) {
            module.setTitle(request.title());
        }

        if (request.position() != null) {
            module.setPosition(request.position());
        }

        return moduleRepository.save(module);
    }

    @Override
    public void delete(UUID modulePublicId) {
        if (!moduleRepository.existsByPublicId(modulePublicId)) {
            throw new NotFoundException("Module introuvable: " + modulePublicId);
        }
        moduleRepository.deleteByPublicId(modulePublicId);
    }
}
