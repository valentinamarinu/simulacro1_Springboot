package com.simulacros.simulacro_1.infrastructure.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.CourseReq;
import com.simulacros.simulacro_1.api.dto.response.CourseResp;
import com.simulacros.simulacro_1.api.dto.response.EnrollmentBasicCourseResp;
import com.simulacros.simulacro_1.api.dto.response.LessonBasicResp;
import com.simulacros.simulacro_1.api.dto.response.MessageCourseResp;
import com.simulacros.simulacro_1.api.dto.response.UserBasicResp;
import com.simulacros.simulacro_1.domain.entities.Course;
import com.simulacros.simulacro_1.domain.entities.Enrollment;
import com.simulacros.simulacro_1.domain.entities.Lesson;
import com.simulacros.simulacro_1.domain.entities.Message;
import com.simulacros.simulacro_1.domain.entities.User;
import com.simulacros.simulacro_1.domain.repositories.CourseRepository;
import com.simulacros.simulacro_1.domain.repositories.UserRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.ICourseService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService implements ICourseService {
    @Autowired
    private final CourseRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public Page<CourseResp> getAll(int page, int size, SortType sortType) {
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
    public CourseResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public CourseResp create(CourseReq request) {
        Course course = this.requestToEntity(request);

        course.setUser(findUser(request.getId_user()));
        course.setLessons(new ArrayList<>());
        course.setMessages(new ArrayList<>());
        course.setEnrollments(new ArrayList<>());

        return this.entityToResponse(this.repository.save(course));
    }

    @Override
    public CourseResp update(CourseReq request, Long id) {
        Course course = this.find(id);

        Course courseUpdate = this.requestToEntity(request);

        courseUpdate.setId_course(id);
        courseUpdate.setUser(course.getUser());
        courseUpdate.setLessons(course.getLessons());
        courseUpdate.setMessages(course.getMessages());
        courseUpdate.setEnrollments(course.getEnrollments());

        return this.entityToResponse(this.repository.save(courseUpdate));
    }

    @Override
    public void delete(Long id) {
        Course course = this.find(id);

        this.repository.delete(course);
    }

    private CourseResp entityToResponse(Course entity) {
        UserBasicResp user = UserBasicResp.builder()
                                .id_user(entity.getUser().getId_user())
                                .username(entity.getUser().getUsername())
                                .password(entity.getUser().getPassword())
                                .email(entity.getUser().getEmail())
                                .full_name(entity.getUser().getFull_name())
                                .role(entity.getUser().getRole())
                                .build();

        List<LessonBasicResp> lessons = entity.getLessons()
                                        .stream()
                                        .map(this::lessonEntityToResponse)
                                        .collect(Collectors.toList());

        List<MessageCourseResp> messages = entity.getMessages()
                                            .stream()
                                            .map(this::messageEntityToResponse)
                                            .collect(Collectors.toList());

        List<EnrollmentBasicCourseResp> enrollments = entity.getEnrollments()
                                                        .stream()
                                                        .map(this::enrollmentsEntityToResponse)
                                                        .collect(Collectors.toList());
        
        return CourseResp.builder()
                .id_course(entity.getId_course())
                .course_name(entity.getCourse_name())
                .description(entity.getDescription())
                .user(user)
                .lessons(lessons)
                .messages(messages)
                .enrollments(enrollments)
                .build();
    }
    
    private LessonBasicResp lessonEntityToResponse(Lesson entity) {
        return LessonBasicResp.builder()
                .id_lesson(entity.getId_lesson())
                .lesson_title(entity.getLesson_title())
                .content(entity.getContent())
                .build();
    }

    private EnrollmentBasicCourseResp enrollmentsEntityToResponse(Enrollment entity) {
        UserBasicResp user = new UserBasicResp();
        if (entity.getUser() != null){
            BeanUtils.copyProperties(entity.getUser(), user);
        }

        return EnrollmentBasicCourseResp.builder()
                .id_enrollment(entity.getId_enrollment())
                .enrollment_date(entity.getEnrollment_date())
                .user(user)
                .build();
    }

    private MessageCourseResp messageEntityToResponse(Message entity) {
        UserBasicResp sender = new UserBasicResp();
        if (entity.getSender() != null){
            BeanUtils.copyProperties(entity.getSender(), sender);
        }

        UserBasicResp receiver = new UserBasicResp();
        if (entity.getReceiver() != null){
            BeanUtils.copyProperties(entity.getReceiver(), receiver);
        }

        return MessageCourseResp.builder()
                .id_message(entity.getId_message())
                .message_content(entity.getMessage_content())
                .sent_date(entity.getSent_date())
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    private Course requestToEntity(CourseReq request) {
        return Course.builder()
                .course_name(request.getCourse_name())
                .description(request.getDescription())
                .user(findUser(request.getId_user()))
                .build();
    }

    private Course find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Course")));
    }

    private User findUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("User")));
    }
}
