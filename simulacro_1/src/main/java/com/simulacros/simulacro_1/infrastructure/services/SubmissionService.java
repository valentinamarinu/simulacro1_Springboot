package com.simulacros.simulacro_1.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.SubmissionReq;
import com.simulacros.simulacro_1.api.dto.response.AssignmentBasicResp;
import com.simulacros.simulacro_1.api.dto.response.LessonBasicResp;
import com.simulacros.simulacro_1.api.dto.response.SubmissionResp;
import com.simulacros.simulacro_1.api.dto.response.UserBasicResp;
import com.simulacros.simulacro_1.domain.entities.Assignment;
import com.simulacros.simulacro_1.domain.entities.Submission;
import com.simulacros.simulacro_1.domain.entities.User;
import com.simulacros.simulacro_1.domain.repositories.AssignmentRepository;
import com.simulacros.simulacro_1.domain.repositories.SubmissionRepository;
import com.simulacros.simulacro_1.domain.repositories.UserRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.ISubmissionService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubmissionService implements ISubmissionService {
    /* Inyección de dependencias */
    @Autowired
    private final SubmissionRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AssignmentRepository assignmentRepository;

    /* Obtengo todas las entregas ordenadas por "submission_date" */
    @Override
    public Page<SubmissionResp> getAll(int page, int size, SortType sortType) {
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
    public SubmissionResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public SubmissionResp create(SubmissionReq request) {
        Submission submission = this.requestToEntity(request);

        submission.setUser(findUser(request.getId_user()));
        submission.setAssignment(findAssignment(request.getId_assignment()));

        return this.entityToResponse(this.repository.save(submission));
    }

    @Override
    public SubmissionResp update(SubmissionReq request, Long id) {
        Submission submission = this.find(id);

        Submission submissionUpdate = this.requestToEntity(request);

        submissionUpdate.setId_submission(id);
        submissionUpdate.setSubmission_date(submission.getSubmission_date());
        submissionUpdate.setUser(submission.getUser());
        submissionUpdate.setAssignment(submission.getAssignment());

        return this.entityToResponse(this.repository.save(submissionUpdate));
    }

    @Override
    public void delete(Long id) {
        Submission submission = this.find(id);

        this.repository.delete(submission);
    }
    
    private SubmissionResp entityToResponse(Submission entity) {
        UserBasicResp user = UserBasicResp.builder()
                            .id_user(entity.getUser().getId_user())
                            .username(entity.getUser().getUsername())
                            .password(entity.getUser().getPassword())
                            .email(entity.getUser().getEmail())
                            .full_name(entity.getUser().getFull_name())
                            .role(entity.getUser().getRole())
                            .build();

        LessonBasicResp lesson = LessonBasicResp.builder()
                                .id_lesson(entity.getAssignment().getLesson().getId_lesson())
                                .lesson_title(entity.getAssignment().getLesson().getLesson_title())
                                .content(entity.getAssignment().getLesson().getContent())
                                .build();
        
        AssignmentBasicResp assignment = AssignmentBasicResp.builder()
                                        .id_assignment(entity.getAssignment().getId_assignment())
                                        .assignment_title(entity.getAssignment().getAssignment_title())
                                        .description(entity.getAssignment().getDescription())
                                        .due_date(entity.getAssignment().getDue_date())
                                        .lesson(lesson)
                                        .build();

        return SubmissionResp.builder()
                .id_submission(entity.getId_submission())
                .content(entity.getContent())
                .submission_date(entity.getSubmission_date())
                .grade(entity.getGrade())
                .user(user)
                .assignment(assignment)
                .build();
    }

    private Submission requestToEntity(SubmissionReq request) {
        return Submission.builder()
                .content(request.getContent())
                .submission_date(request.getSubmission_date())
                .grade(request.getGrade())
                .user(findUser(request.getId_user()))
                .assignment(findAssignment(request.getId_assignment()))
                .build();
    }
    
    private Submission find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Submission")));
    }

    private User findUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("User")));
    }

    private Assignment findAssignment(Long id) {
        return this.assignmentRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Assignment")));
    }
}
