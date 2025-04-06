--------------------------------------------------------------------------
-- Table: users
-- Columns: username, email, phone_number, password, role
--------------------------------------------------------------------------
INSERT INTO users (username, email, phone_number, password, role)
VALUES
('adminUser', 'admin@example.com', '555001', 'encodedAdminPass', 'ADMIN'),
('studentUser', 'student@example.com', '555002', 'encodedStudentPass', 'STUDENT'),
('instructorUser', 'instructor@example.com', '555003', 'encodedInstructorPass', 'INSTRUCTOR'),
('donorUser', 'donor@example.com', '555004', 'encodedDonorPass', 'DONOR');

--------------------------------------------------------------------------
-- Table: admins
-- Columns: user_id, department, admin_level
--------------------------------------------------------------------------
INSERT INTO admins (user_id, department, admin_level)
VALUES
(1, 'IT Department', 'Level 1');

--------------------------------------------------------------------------
-- Table: students
-- Columns: user_id, major, grade_level, gpa
--------------------------------------------------------------------------
INSERT INTO students (user_id, major, grade_level, gpa)
VALUES
(2, 'Computer Science', 'Senior', 3.45);

--------------------------------------------------------------------------
-- Table: instructors
-- Columns: user_id, specialization, years_of_experience
--------------------------------------------------------------------------
INSERT INTO instructors (user_id, specialization, years_of_experience)
VALUES
(3, 'Software Engineering', 5);

--------------------------------------------------------------------------
-- Table: donors
-- Columns: user_id, organization_name
--------------------------------------------------------------------------
INSERT INTO donors (user_id, organization_name)
VALUES
(4, 'Tech Donors Inc.');

--------------------------------------------------------------------------
-- Table: scholarships
-- Columns: name, description, status
--------------------------------------------------------------------------
INSERT INTO scholarships (name, description, status)
VALUES
('Merit Scholarship', 'Scholarship for high-achieving students', 'OPEN'),
('Need-based Scholarship', 'Financial aid for students in need', 'OPEN');

--------------------------------------------------------------------------
-- Table: courses
-- Columns: title, description, is_free, price, instructor_id
--------------------------------------------------------------------------
INSERT INTO courses (title, description, is_free, price, instructor_id)
VALUES
('Java Fundamentals', 'Introductory course on Java SE.', true, NULL, 1),
('Advanced Databases', 'Deep dive into SQL and NoSQL systems.', false, 399.99, 1);

--------------------------------------------------------------------------
-- Table: contents
-- Columns: title, url, type, completed, course_id
--------------------------------------------------------------------------
INSERT INTO contents (title, url, type, completed, course_id)
VALUES
('Intro Video', 'https://example.com/java-intro.mp4', 'video', false, 1),
('Slides Week 1', 'https://example.com/week1-slides.pdf', 'pdf', false, 1),
('Databases Lecture', 'https://example.com/db-lecture.mp4', 'video', false, 2);

--------------------------------------------------------------------------
-- Table: donations
-- Columns: amount, note, donation_date, donor_id, scholarship_id, course_id
--------------------------------------------------------------------------
INSERT INTO donations (amount, note, donation_date, donor_id, scholarship_id, course_id)
VALUES
(500.00, 'Donation for Merit Scholarship', '2025-03-30 10:00:00', 1, 1, NULL),
(250.00, 'Donation for Java course', '2025-03-30 11:15:00', 1, NULL, 1);

--------------------------------------------------------------------------
-- Table: enrollments
-- Columns: student_id, course_id, enrollment_date
--------------------------------------------------------------------------
INSERT INTO enrollments (student_id, course_id, enrollment_date)
VALUES
(1, 1, '2025-03-15'),
(1, 2, '2025-03-20');

--------------------------------------------------------------------------
-- Table: attendance
-- Columns: week_number, present, note, student_id, course_id
--------------------------------------------------------------------------
INSERT INTO attendance (week_number, present, note, student_id, course_id)
VALUES
(1, true, 'Attended the first lecture', 1, 1),
(1, false, 'Missed the first lecture', 1, 2);

--------------------------------------------------------------------------
-- Table: assessments
-- Columns: type, status, correct_answers, course_id
--------------------------------------------------------------------------
INSERT INTO assessments (type, status, correct_answers, course_id)
VALUES
('Quiz', 'OPEN', 'A,B,D', 1),
('Assignment', 'OPEN', NULL, 1),
('Final Exam', 'OPEN', NULL, 2);
