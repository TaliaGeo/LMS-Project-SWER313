package com.example.lms.student;

import java.util.List;

import com.example.lms.course.Course;
import com.example.lms.user.User;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student createStudent(Student student);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
    void deleteDuplicateStudentsForCurrentUser(String username);
    Student createStudent(Student student, User user);
    Student getStudentByUsername(String username);
    List<Course> getCoursesForStudent(Long studentId);
    Student findByUser(User user);

}