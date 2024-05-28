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

import com.simulacros.simulacro_1.api.dto.request.UserReq;
import com.simulacros.simulacro_1.api.dto.response.AssignmentBasicResp;
import com.simulacros.simulacro_1.api.dto.response.CourseBasicResp;
import com.simulacros.simulacro_1.api.dto.response.EnrollmentBasicResp;
import com.simulacros.simulacro_1.api.dto.response.MessageReceiverResp;
import com.simulacros.simulacro_1.api.dto.response.MessageSenderResp;
import com.simulacros.simulacro_1.api.dto.response.SubmissionBasicResp;
import com.simulacros.simulacro_1.api.dto.response.UserBasicResp;
import com.simulacros.simulacro_1.api.dto.response.UserResp;
import com.simulacros.simulacro_1.domain.entities.Course;
import com.simulacros.simulacro_1.domain.entities.Enrollment;
import com.simulacros.simulacro_1.domain.entities.Message;
import com.simulacros.simulacro_1.domain.entities.Submission;
import com.simulacros.simulacro_1.domain.entities.User;
import com.simulacros.simulacro_1.domain.repositories.UserRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.IUserService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    /* Inyección de dependencias */
    @Autowired
    private final UserRepository repository;

    /* Obtener todos los usuarios y listarlos por orden de "full_name" */
    @Override
    public Page<UserResp> getAll(int page, int size, SortType sortType) {
        /* Se valida que no se tiene un número de página negativo, si es así se asigna como página incial la cero */
        if (page < 0) page = 0;

        /* Creo la variable pagination que será la que dependiendo del tipo de ordenamiento me devolvera la forma de paginación */
        PageRequest pagination = null;

        /* Lógica de clasificación de ordenamiento */
        switch (sortType) {
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }

        /* Retorna todos la respuesta de todos los usuarios organizados según la clasificación asignada por los parametros de entrada */
        return this.repository.findAll(pagination).map(this::entityToResponse);
    }

    /* Obtener información detallada de un usuario por id */
    @Override
    public UserResp get(Long id) {
        /* Devuelvo la respuesta de usuario encontrada por el id */
        return this.entityToResponse(this.find(id));
    }

    /* Registrar usuario */
    @Override
    public UserResp create(UserReq request) {
        /* Almaceno en una entidad la información recibida del request */
        User user = this.requestToEntity(request);
        
        /* Creo listas vacias para las relaciones OneToMany que no se incluyen en el request */
        user.setCourses(new ArrayList<>());
        user.setMessages_sender(new ArrayList<>());
        user.setMessages_receiver(new ArrayList<>());
        user.setEnrollments(new ArrayList<>());
        user.setSubmissions(new ArrayList<>());

        /* Guardo la nueva entidad usuario en el repositorio */
        return this.entityToResponse(this.repository.save(user));
    }

    /* Actualizar usuario */
    @Override
    public UserResp update(UserReq request, Long id) {
        /* Encuentro el usuario que deseo actualizar */
        User user = this.find(id);

        /* Almaceno en una entidad actualizada la información que actualizo por medio del request */
        User userUpdate = this.requestToEntity(request);

        /* Recupero toda la información que no se va a actualizar y la almaceno en la entidad actualizada */
        userUpdate.setId_user(id);
        userUpdate.setCourses(user.getCourses());
        userUpdate.setMessages_sender(user.getMessages_sender());
        userUpdate.setMessages_receiver(user.getMessages_receiver());
        userUpdate.setEnrollments(user.getEnrollments());
        userUpdate.setSubmissions(user.getSubmissions());

        /* Guardo la entidad actualizada en el repositorio */
        return this.entityToResponse(this.repository.save(userUpdate));
    }

    /* Eliminar usuario */
    @Override
    public void delete(Long id) {
        /* Encuentro el usuario que quiero eliminar por id */
        User user = this.find(id);

        /* Elimino del repositorio el usuario encontrado */
        this.repository.delete(user);
    }

    /* Convertir entidad de usuario en respuesta */
    private UserResp entityToResponse(User entity) {
        /* Creo la lista de respuesta de cursos */
        List<CourseBasicResp> courses = 
            entity.getCourses()
            .stream()
            .map(this::courseEntityToResponse)
            .collect(Collectors.toList());

        /* Creo la lista de respuesda de mensajes recibidos */
        List<MessageSenderResp> messages_sender = 
            entity.getMessages_sender()
            .stream()
            .map(this::messageSenderEntityToResponse)
            .collect(Collectors.toList());

        /* Creo la lista de respuesta de mensajes enviados */
        List<MessageReceiverResp> messages_receiver = 
            entity.getMessages_receiver()
            .stream()
            .map(this::messageReceiverEntityToResponse)
            .collect(Collectors.toList());

        /* Creo la lista de respuesta de inscripciones */
        List<EnrollmentBasicResp> enrollments = 
            entity.getEnrollments()
            .stream()
            .map(this::enrollmentEntityToResponse)
            .collect(Collectors.toList());  

        /* Creo la lista de respuesta de entregas */
        List<SubmissionBasicResp> submissions = 
            entity.getSubmissions()
            .stream()
            .map(this::submissionEntityToResponse)
            .collect(Collectors.toList());  
            
        /* Construyo la respuesta de usuario */
        return UserResp.builder()
                .id_user(entity.getId_user())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .role(entity.getRole())
                .courses(courses)
                .messages_sender(messages_sender)
                .messages_receiver(messages_receiver)
                .enrollments(enrollments)
                .submissions(submissions)
                .build();
    }

    /* Convertir entidad de curso en respuesta */
    private CourseBasicResp courseEntityToResponse(Course entity){
        /* Construyo la respuesta de cursos */
        return CourseBasicResp.builder()
                .id_course(entity.getId_course())
                .course_name(entity.getCourse_name())
                .description(entity.getDescription())
                .build();
    }

    /* Convertir entidad de mensajes que envia en respuesta */
    private MessageSenderResp messageSenderEntityToResponse(Message entity) {
        /* Creo una nueva respuesta del usuario al que le envia el mensaje */
        UserBasicResp receiver = new UserBasicResp();
        /* Se copian las propiedades del destinatario del mensaje original al objeto receiver recién creado, además se valida que no sea nulo para que BeanUtils no lance error  */
        if (entity.getReceiver() != null) {
            BeanUtils.copyProperties(entity.getReceiver(), receiver);
        }

        /* Se contruye la respuesta del mensaje que envia */
        return MessageSenderResp.builder()
                .id_message(entity.getId_message())
                .message_content(entity.getMessage_content())
                .receiver(receiver)
                .build();
    }

    /* Convertir entidad de mensajes que recibe en respuesta */
    private MessageReceiverResp messageReceiverEntityToResponse(Message entity) {
        /* Creo una nueva respuesta del usuario que le envia el mensaje */
        UserBasicResp sender = new UserBasicResp();
        /* Se copian las propiedades del remitente del mensaje original al objeto sender recién creado, además se valida que no sea nulo para que BeanUtils no lance error */
        if (entity.getSender() != null){
            BeanUtils.copyProperties(entity.getSender(), sender);
        }

        /* Se contruye la respuesta del mensaje que recibe  */
        return MessageReceiverResp.builder()
                .id_message(entity.getId_message())
                .message_content(entity.getMessage_content())
                .sender(sender)
                .build();
    }

    /* Convertir entidad de inscripciones en respuesta */
    private EnrollmentBasicResp enrollmentEntityToResponse(Enrollment entity) {
        /* Se contruye la respuesta de la inscripciones */
        return EnrollmentBasicResp.builder()
                .id_enrollment(entity.getId_enrollment())
                .enrollment_date(entity.getEnrollment_date())
                .build();
    }

    /* Convertir entidad de entrega en respuesta */
    private SubmissionBasicResp submissionEntityToResponse(Submission entity) {
        /* Creo una nueva respuesta de entrega */
        AssignmentBasicResp assignment = new AssignmentBasicResp();
        /* Se copian las propiedades de la entrega original al objeto assignment recién creado, además se valida que no sea nulo para que BeanUtils no lance error  */
        if (entity.getAssignment() != null) {
            BeanUtils.copyProperties(entity.getAssignment(), assignment);
        }

        /* Se contruye la respuesta de entrega */
        return SubmissionBasicResp.builder()
                .id_submission(entity.getId_submission())
                .content(entity.getContent())
                .submission_date(entity.getSubmission_date())
                .grade(entity.getGrade())
                .assignment(assignment)
                .build();
    }

    /* Convertir requerimiento de usuario en entidad */
    private User requestToEntity(UserReq request) {
        /* Se construye la entidad de usuario */
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .full_name(request.getFull_name())
                .role(request.getRole())
                .build();
    }

    /* Encuentro un usuario por id */
    private User find(Long id) {
        /* Se busca en el repositorio por el id enviado y se arroja error si no se encuentra dicho usuario */
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("User")));
    }
}
