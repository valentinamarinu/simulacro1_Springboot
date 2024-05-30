package com.simulacros.simulacro_1.infrastructure.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.LessonReq;
import com.simulacros.simulacro_1.api.dto.response.AssignmentBasicLessonResp;
import com.simulacros.simulacro_1.api.dto.response.CourseBasicResp;
import com.simulacros.simulacro_1.api.dto.response.LessonResp;
import com.simulacros.simulacro_1.domain.entities.Assignment;
import com.simulacros.simulacro_1.domain.entities.Course;
import com.simulacros.simulacro_1.domain.entities.Lesson;
import com.simulacros.simulacro_1.domain.repositories.CourseRepository;
import com.simulacros.simulacro_1.domain.repositories.LessonRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.ILessonService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LessonService implements ILessonService {
    @Autowired
    private final LessonRepository repository;

    @Autowired
    private final CourseRepository courseRepository;
    
    @Override
    public Page<LessonResp> getAll(int page, int size, SortType sortType) {
        if (page < 0) page = 0;

        PageRequest pagination = null;

        switch (sortType) {
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.repository.findAll(pagination).map(this::entityToResponse);
    }

    @Override
    public LessonResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public LessonResp create(LessonReq request) {
        Lesson lesson = this.requestToEntity(request);

        lesson.setAssignments(new ArrayList<>());
        lesson.setCourse(findCourse(request.getId_course()));

        return this.entityToResponse(this.repository.save(lesson));
    }

    @Override
    public LessonResp update(LessonReq request, Long id) {
        Lesson lesson = this.find(id);

        Lesson lessonUpdate = this.requestToEntity(request);

        lessonUpdate.setId_lesson(id);
        lessonUpdate.setAssignments(lesson.getAssignments());
        lessonUpdate.setCourse(lesson.getCourse());

        return this.entityToResponse(this.repository.save(lessonUpdate));
    }

    @Override
    public void delete(Long id) {
        Lesson lesson = this.find(id);

        this.repository.delete(lesson);
    }
    

    private LessonResp entityToResponse(Lesson entity) {
        List<AssignmentBasicLessonResp> assignments = 
            entity.getAssignments()
            .stream()
            .map(this::assignmentEntityToResponse)
            .collect(Collectors.toList()); 
        
        CourseBasicResp course = CourseBasicResp.builder()
                        .id_course(entity.getCourse().getId_course())
                        .course_name(entity.getCourse().getCourse_name())
                        .description(entity.getCourse().getDescription())
                        .build();

        return LessonResp.builder()
                .id_lesson(entity.getId_lesson())
                .lesson_title(entity.getLesson_title())
                .content(entity.getContent())
                .assignments(assignments)
                .course(course)
                .build();
    }

    private Lesson requestToEntity(LessonReq request) {
        return Lesson.builder()
                .lesson_title(request.getLesson_title())
                .content(request.getContent())
                .course(findCourse(request.getId_course()))
                .build();
    }

    private AssignmentBasicLessonResp assignmentEntityToResponse(Assignment entity){
        return AssignmentBasicLessonResp.builder()
                .id_assignment(entity.getId_assignment())
                .assignment_title(entity.getAssignment_title())
                .description(entity.getDescription())
                .build();
    }

    private Lesson find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Lesson")));
    }

    private Course findCourse(Long id) {
        return this.courseRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Course")));
    }
}
