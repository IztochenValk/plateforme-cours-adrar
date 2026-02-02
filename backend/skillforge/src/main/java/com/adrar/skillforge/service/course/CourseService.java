package com.adrar.skillforge.service.course;

import com.adrar.skillforge.dto.course.CourseTreeResponse;
import com.adrar.skillforge.dto.course.CreateCourseRequest;
import com.adrar.skillforge.dto.course.PatchCourseRequest;
import com.adrar.skillforge.dto.course.UpdateCourseRequest;
import com.adrar.skillforge.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    Course findByPublicId(UUID publicId);

    Page<Course> findAll(Pageable pageable);

    Course create(CreateCourseRequest request);

    Course update(UUID publicId, UpdateCourseRequest request);

    Course patch(UUID publicId, PatchCourseRequest request);

    void deleteByPublicId(UUID publicId);

    CourseTreeResponse getTree(UUID coursePublicId);

    List<CourseTreeResponse> getAllTrees();

    Page<CourseTreeResponse> getTrees(Pageable pageable);
}
