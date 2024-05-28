package com.simulacros.simulacro_1.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulacros.simulacro_1.domain.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
}
