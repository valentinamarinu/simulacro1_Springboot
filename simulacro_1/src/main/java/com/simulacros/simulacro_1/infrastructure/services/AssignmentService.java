package com.simulacros.simulacro_1.infrastructure.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.AssignmentReq;
import com.simulacros.simulacro_1.api.dto.response.AssignmentResp;
import com.simulacros.simulacro_1.api.dto.response.LessonBasicResp;
import com.simulacros.simulacro_1.api.dto.response.SubmissionBasicAssignmentResp;
import com.simulacros.simulacro_1.domain.entities.Assignment;
import com.simulacros.simulacro_1.domain.entities.Lesson;
import com.simulacros.simulacro_1.domain.entities.Submission;
import com.simulacros.simulacro_1.domain.repositories.AssignmentRepository;
import com.simulacros.simulacro_1.domain.repositories.LessonRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.IAssignmentService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AssignmentService implements IAssignmentService{
    @Autowired
    private final AssignmentRepository repository;

    @Autowired
    private final LessonRepository lessonRepository;
    
    @Override
    public Page<AssignmentResp> getAll(int page, int size, SortType sortType) {
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
    public AssignmentResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public AssignmentResp create(AssignmentReq request) {
        Assignment assignment = this.requestToEntity(request);

        assignment.setLesson(findLesson(request.getId_lesson()));
        assignment.setSubmissions(new ArrayList<>());

        return this.entityToResponse(this.repository.save(assignment));
    }

    @Override
    public AssignmentResp update(AssignmentReq request, Long id) {
        Assignment assignment = this.find(id);
        
        Assignment assignmentUpdate = this.requestToEntity(request);

        assignmentUpdate.setId_assignment(id);
        assignmentUpdate.setLesson(assignment.getLesson());
        assignmentUpdate.setSubmissions(assignment.getSubmissions());

        return this.entityToResponse(this.repository.save(assignmentUpdate));
    }

    @Override
    public void delete(Long id) {
        Assignment assignment = this.find(id);

        this.repository.delete(assignment);
    }

    private AssignmentResp entityToResponse(Assignment entity) {
        LessonBasicResp lesson =  LessonBasicResp.builder()
                                .id_lesson(entity.getLesson().getId_lesson())
                                .lesson_title(entity.getLesson().getLesson_title())
                                .content(entity.getLesson().getContent())
                                .build();

        List<SubmissionBasicAssignmentResp> submissions = entity.getSubmissions()
                                                        .stream()
                                                        .map(this::submissionEntityToResponse)
                                                        .collect(Collectors.toList());
        

        return AssignmentResp.builder()
                .id_assignment(entity.getId_assignment())
                .assignment_title(entity.getAssignment_title())
                .description(entity.getDescription())
                .due_date(entity.getDue_date())
                .lesson(lesson)
                .submissions(submissions)
                .build();
    }

    private SubmissionBasicAssignmentResp submissionEntityToResponse(Submission entity) {
        return SubmissionBasicAssignmentResp.builder()
                .id_submission(entity.getId_submission())
                .content(entity.getContent())
                .submission_date(entity.getSubmission_date())
                .grade(entity.getGrade())
                .build();
    }

    private Assignment requestToEntity(AssignmentReq request) {
        return Assignment.builder()
                .assignment_title(request.getAssignment_title())
                .description(request.getDescription())
                .due_date(request.getDue_date())
                .lesson(findLesson(request.getId_lesson()))
                .build();
    }

    private Assignment find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Assignment")));
    }

    private Lesson findLesson(Long id) {
        return this.lessonRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Lesson")));
    }
}
