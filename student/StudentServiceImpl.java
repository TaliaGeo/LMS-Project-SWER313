package com.example.lms.student;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.course.Course;
import com.example.lms.enrollment.EnrollmentRepository;
import com.example.lms.enrollment.Enrollment;
import com.example.lms.exceptions.ResourceNotFoundException;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository; // Fixed Injection

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, 
                               EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Course> getCoursesForStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Student createStudent(Student student) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        student.setUser(user);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = getStudentById(id);
        existingStudent.setMajor(updatedStudent.getMajor());
        return studentRepository.save(existingStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student getStudentByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found for user ID: " + user.getId()));
    }

    @Override
    @Transactional
    public void deleteDuplicateStudentsForCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Student> all = studentRepository.findAllByUser(user);
        if (all.size() > 1) {
            for (int i = 1; i < all.size(); i++) {
                studentRepository.delete(all.get(i));
            }
        }
    }

    @Override
    @Transactional
    public Student createStudent(Student student, User user) {
        student.setUser(user);
        return studentRepository.save(student);
    }
    @Override
    public Student findByUser(User user) {
        return studentRepository.findByUser(user).orElse(null);
}

}
