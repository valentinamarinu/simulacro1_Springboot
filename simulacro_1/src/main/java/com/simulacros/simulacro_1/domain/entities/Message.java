package com.simulacros.simulacro_1.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "messages")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /* Atributos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_message;
    
    /* @Column(columnDefinition = "TEXT") */
    @Lob
    private String message_content;

    private LocalDateTime sent_date;

    /* Relación con tabla "Users" del remitente */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id_user")
    private User sender;

    /* Relación con tabla "Users" del destinatario */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id_user")
    private User receiver;
 
    /* Relación con tabla "Course" */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id_course")
    private Course course;
}
