package com.simulacros.simulacro_1.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.EnrollmentReq;
import com.simulacros.simulacro_1.api.dto.response.CourseBasicResp;
import com.simulacros.simulacro_1.api.dto.response.EnrollmentResp;
import com.simulacros.simulacro_1.api.dto.response.UserBasicResp;
import com.simulacros.simulacro_1.domain.entities.Course;
import com.simulacros.simulacro_1.domain.entities.Enrollment;
import com.simulacros.simulacro_1.domain.entities.User;
import com.simulacros.simulacro_1.domain.repositories.CourseRepository;
import com.simulacros.simulacro_1.domain.repositories.EnrollmentRepository;
import com.simulacros.simulacro_1.domain.repositories.UserRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.IEnrollmentService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnrollmentService implements IEnrollmentService {
    @Autowired
    private final EnrollmentRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CourseRepository courseRepository;

    @Override
    public Page<EnrollmentResp> getAll(int page, int size, SortType sortType) {
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
    public EnrollmentResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public EnrollmentResp create(EnrollmentReq request) {
        Enrollment enrollment = this.requestToEntity(request);

        enrollment.setUser(findUser(request.getId_course()));
        enrollment.setCourse(findCourse(request.getId_course()));
        
        return this.entityToResponse(this.repository.save(enrollment));
    }

    @Override
    public EnrollmentResp update(EnrollmentReq request, Long id) {
        Enrollment enrollment = this.find(id);

        Enrollment enrollmentUpdate = this.requestToEntity(request);

        enrollmentUpdate.setId_enrollment(id);
        enrollmentUpdate.setUser(enrollment.getUser());
        enrollmentUpdate.setCourse(enrollment.getCourse());

        return this.entityToResponse(this.repository.save(enrollmentUpdate));
    }

    @Override
    public void delete(Long id) {
        Enrollment enrollment = this.find(id);

        this.repository.delete(enrollment);
    }

    private EnrollmentResp entityToResponse(Enrollment entity) {
        UserBasicResp user = UserBasicResp.builder()
                                .id_user(entity.getUser().getId_user())
                                .username(entity.getUser().getUsername())
                                .password(entity.getUser().getPassword())
                                .email(entity.getUser().getEmail())
                                .full_name(entity.getUser().getFull_name())
                                .role(entity.getUser().getRole())
                                .build();

        CourseBasicResp course = CourseBasicResp.builder()
                                .id_course(entity.getCourse().getId_course())
                                .course_name(entity.getCourse().getCourse_name())
                                .description(entity.getCourse().getDescription())
                                .build();

        return EnrollmentResp.builder()
                .id_enrollment(entity.getId_enrollment())
                .enrollment_date(entity.getEnrollment_date())
                .user(user)
                .course(course)
                .build();
    }

    private Enrollment requestToEntity(EnrollmentReq request) {
        return Enrollment.builder()
                .enrollment_date(request.getEnrollment_date())
                .user(findUser(request.getId_user()))
                .course(findCourse(request.getId_course()))
                .build();
    }

    private Enrollment find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Enrollment")));
    }

    private User findUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("User")));
    }

    private Course findCourse(Long id) {
        return this.courseRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Course")));
    }
}
