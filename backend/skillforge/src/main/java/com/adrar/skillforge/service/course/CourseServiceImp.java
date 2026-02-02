package com.adrar.skillforge.service.course;

import com.adrar.skillforge.dto.course.CourseTreeResponse;
import com.adrar.skillforge.dto.course.CreateCourseRequest;
import com.adrar.skillforge.dto.course.LessonTreeResponse;
import com.adrar.skillforge.dto.course.ModuleTreeResponse;
import com.adrar.skillforge.dto.course.PatchCourseRequest;
import com.adrar.skillforge.dto.course.UpdateCourseRequest;
import com.adrar.skillforge.entity.Course;
import com.adrar.skillforge.entity.Lesson;
import com.adrar.skillforge.entity.Module;
import com.adrar.skillforge.exception.NotFoundException;
import com.adrar.skillforge.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImp implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImp(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course findByPublicId(UUID publicId) {
        return courseRepository.findByPublicId(publicId)
            .orElseThrow(() -> new NotFoundException("Cours introuvable: " + publicId));
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course create(CreateCourseRequest request) {
        Course course = new Course(request.title(), request.description());
        return courseRepository.save(course);
    }

    @Override
    public Course update(UUID publicId, UpdateCourseRequest request) {
        Course course = findByPublicId(publicId);
        course.setTitle(request.title());
        course.setDescription(request.description());
        return courseRepository.save(course);
    }

    @Override
    public Course patch(UUID publicId, PatchCourseRequest request) {
        Course course = findByPublicId(publicId);

        if (request.title() != null && !request.title().isBlank()) {
            course.setTitle(request.title());
        }
        if (request.description() != null) {
            course.setDescription(request.description());
        }

        return courseRepository.save(course);
    }

    @Override
    public void deleteByPublicId(UUID publicId) {
        if (!courseRepository.existsByPublicId(publicId)) {
            throw new NotFoundException("Cours introuvable: " + publicId);
        }
        courseRepository.deleteByPublicId(publicId);
    }

    @Override
    public CourseTreeResponse getTree(UUID coursePublicId) {
        Course course = courseRepository.findTreeByPublicId(coursePublicId)
            .orElseThrow(() -> new NotFoundException("Cours introuvable: " + coursePublicId));

        return toTree(course);
    }

    @Override
    public List<CourseTreeResponse> getAllTrees() {
        List<Course> courses = courseRepository.findAllTrees();

        List<Course> uniqueCourses = dedupeCourses(courses).stream()
            .sorted(Comparator.comparing(Course::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
            .toList();

        return uniqueCourses.stream()
            .map(this::toTree)
            .toList();
    }

    @Override
    public Page<CourseTreeResponse> getTrees(Pageable pageable) {
        Page<Course> page = courseRepository.findAll(pageable);
        List<Course> pageCourses = page.getContent();

        if (pageCourses.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, page.getTotalElements());
        }

        List<UUID> publicIds = pageCourses.stream()
            .map(Course::getPublicId)
            .filter(id -> id != null)
            .toList();

        if (publicIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, page.getTotalElements());
        }

        List<Course> trees = courseRepository.findTreesByPublicIds(publicIds);

        Map<UUID, Course> byId = dedupeCourses(trees).stream()
            .collect(Collectors.toMap(Course::getPublicId, c -> c));

        List<CourseTreeResponse> dtoOrdered = publicIds.stream()
            .map(byId::get)
            .filter(c -> c != null)
            .map(this::toTree)
            .toList();

        return new PageImpl<>(dtoOrdered, pageable, page.getTotalElements());
    }

    private CourseTreeResponse toTree(Course course) {
        List<Module> modules = dedupeModules(course.getModules()).stream()
            .sorted(Comparator.comparing(Module::getPosition, Comparator.nullsLast(Integer::compareTo)))
            .toList();

        List<ModuleTreeResponse> moduleDtos = modules.stream()
            .map(m -> {
                List<Lesson> lessons = dedupeLessons(m.getLessons()).stream()
                    .sorted(Comparator.comparing(Lesson::getPosition, Comparator.nullsLast(Integer::compareTo)))
                    .toList();

                List<LessonTreeResponse> lessonDtos = lessons.stream()
                    .map(l -> new LessonTreeResponse(
                        l.getPublicId(),
                        l.getTitle(),
                        l.getPosition(),
                        l.getContent(),
                        l.getCreatedAt()
                    ))
                    .toList();

                return new ModuleTreeResponse(
                    m.getPublicId(),
                    m.getTitle(),
                    m.getPosition(),
                    m.getCreatedAt(),
                    lessonDtos
                );
            })
            .toList();

        return new CourseTreeResponse(
            course.getPublicId(),
            course.getTitle(),
            course.getDescription(),
            course.getCreatedAt(),
            moduleDtos
        );
    }

    private List<Course> dedupeCourses(List<Course> courses) {
        if (courses == null || courses.isEmpty()) return List.of();

        LinkedHashMap<UUID, Course> map = new LinkedHashMap<>();
        for (Course c : courses) {
            if (c != null && c.getPublicId() != null && !map.containsKey(c.getPublicId())) {
                map.put(c.getPublicId(), c);
            }
        }
        return List.copyOf(map.values());
    }

    private List<Module> dedupeModules(List<Module> modules) {
        if (modules == null || modules.isEmpty()) return List.of();

        LinkedHashMap<UUID, Module> map = new LinkedHashMap<>();
        for (Module m : modules) {
            if (m != null && m.getPublicId() != null && !map.containsKey(m.getPublicId())) {
                map.put(m.getPublicId(), m);
            }
        }
        return List.copyOf(map.values());
    }

    private List<Lesson> dedupeLessons(List<Lesson> lessons) {
        if (lessons == null || lessons.isEmpty()) return List.of();

        LinkedHashMap<UUID, Lesson> map = new LinkedHashMap<>();
        for (Lesson l : lessons) {
            if (l != null && l.getPublicId() != null && !map.containsKey(l.getPublicId())) {
                map.put(l.getPublicId(), l);
            }
        }
        return List.copyOf(map.values());
    }
}
