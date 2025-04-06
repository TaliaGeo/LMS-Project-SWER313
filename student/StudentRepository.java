package com.example.lms.student;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.user.User;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserUsername(String username);
    List<Student> findAllByUser(User user);
    Optional<Student> findByUser(User user);
}

