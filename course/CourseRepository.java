package com.example.lms.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  
public interface CourseRepository extends JpaRepository<Course, Long> {
}
