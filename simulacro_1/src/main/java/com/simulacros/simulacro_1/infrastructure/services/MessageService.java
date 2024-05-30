package com.simulacros.simulacro_1.infrastructure.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.MessageReq;
import com.simulacros.simulacro_1.api.dto.response.CourseBasicMessageResp;
import com.simulacros.simulacro_1.api.dto.response.EnrollmentBasicResp;
import com.simulacros.simulacro_1.api.dto.response.LessonBasicResp;
import com.simulacros.simulacro_1.api.dto.response.MessageResp;
import com.simulacros.simulacro_1.api.dto.response.UserBasicResp;
import com.simulacros.simulacro_1.domain.entities.Course;
import com.simulacros.simulacro_1.domain.entities.Enrollment;
import com.simulacros.simulacro_1.domain.entities.Lesson;
import com.simulacros.simulacro_1.domain.entities.Message;
import com.simulacros.simulacro_1.domain.entities.User;
import com.simulacros.simulacro_1.domain.repositories.CourseRepository;
import com.simulacros.simulacro_1.domain.repositories.MessageRepository;
import com.simulacros.simulacro_1.domain.repositories.UserRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.IMessageService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService implements IMessageService {
    @Autowired
    private final MessageRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CourseRepository courseRepository;

    @Override
    public Page<MessageResp> getAll(int page, int size, SortType sortType) {
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
    public MessageResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public MessageResp create(MessageReq request) {
        Message message = this.requestToEntity(request);

        message.setSender(findUser(request.getId_sender()));
        message.setReceiver(findUser(request.getId_receiver()));
        message.setCourse(findCourse(request.getId_course()));

        return this.entityToResponse(this.repository.save(message));
    }

    @Override
    public MessageResp update(MessageReq request, Long id) {
        Message message = this.find(id);

        Message messageUpdate = this.requestToEntity(request);

        messageUpdate.setId_message(id);
        messageUpdate.setSender(message.getSender());
        messageUpdate.setReceiver(message.getReceiver());
        messageUpdate.setCourse(message.getCourse());

        return this.entityToResponse(this.repository.save(messageUpdate));
    }

    @Override
    public void delete(Long id) {
        Message message = this.find(id);

        this.repository.delete(message);
    }

    private MessageResp entityToResponse(Message entity) {
        UserBasicResp sender = UserBasicResp.builder()
                                .id_user(entity.getSender().getId_user())
                                .username(entity.getSender().getUsername())
                                .password(entity.getSender().getPassword())
                                .email(entity.getSender().getEmail())
                                .full_name(entity.getSender().getFull_name())
                                .role(entity.getSender().getRole())
                                .build();

        UserBasicResp receiver = UserBasicResp.builder()
                                .id_user(entity.getReceiver().getId_user())
                                .username(entity.getReceiver().getUsername())
                                .password(entity.getReceiver().getPassword())
                                .email(entity.getReceiver().getEmail())
                                .full_name(entity.getReceiver().getFull_name())
                                .role(entity.getReceiver().getRole())
                                .build();

        List<LessonBasicResp> lessons = entity.getCourse().getLessons().stream().map(this::lessonEntityToResponse).collect(Collectors.toList());

        List<EnrollmentBasicResp> enrollments = entity.getCourse().getEnrollments().stream().map(this::enrollmentEntityToResponse).collect(Collectors.toList());

        CourseBasicMessageResp course = CourseBasicMessageResp.builder()
                                        .id_course(entity.getCourse().getId_course())
                                        .course_name(entity.getCourse().getCourse_name())
                                        .lessons(lessons)
                                        .enrollments(enrollments)
                                        .build();
        
        return MessageResp.builder()
                .id_message(entity.getId_message())
                .message_content(entity.getMessage_content())
                .sent_date(entity.getSent_date())
                .sender(sender)
                .receiver(receiver)
                .course(course)
                .build();
    }

    private LessonBasicResp lessonEntityToResponse(Lesson entity) {
        return LessonBasicResp.builder()
                .id_lesson(entity.getId_lesson())
                .lesson_title(entity.getLesson_title())
                .content(entity.getContent())
                .build();
    }

    private EnrollmentBasicResp enrollmentEntityToResponse(Enrollment entity) {
        return EnrollmentBasicResp.builder()
                .id_enrollment(entity.getId_enrollment())
                .enrollment_date(entity.getEnrollment_date())
                .build();
    }

    private Message requestToEntity(MessageReq request) {
        return Message.builder()
                .message_content(request.getMessage_content())
                .sender(findUser(request.getId_sender()))
                .receiver(findUser(request.getId_receiver()))
                .course(findCourse(request.getId_course()))
                .build();
    }

    private Message find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Message")));
    }

    private User findUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("User")));
    }

    private Course findCourse(Long id) {
        return this.courseRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Course")));
    }
}
