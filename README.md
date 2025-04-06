# LMS Project – SWER313 / Spring 2025

A **Monolithic Spring Boot application** for managing an educational platform, implementing user roles (Admin, Student, Instructor, Donor), course management, enrollments, content delivery, assessments, notifications, and Google OAuth2 login.

---

## Table of Contents
1. [Overview](#overview)  
2. [Requirements](#requirements)  
3. [Features & Modules](#features-and-modules)  
4. [Project Structure](#project-structure)  
5. [Installation & Setup](#installation--setup)  
   - [Database Setup](#1-database-setup)  
   - [JWT Secret](#2-jwt-secret-key)  
   - [Email SMTP](#3-email-smtp-settings)  
   - [Google OAuth2](#4-google-oauth2-settings)  
6. [Running the Project](#running-the-project)  
7. [Authentication & Authorization](#authentication--authorization)  
8. [API Testing (Postman)](#api-testing-with-postman)  
9. [Entity Relationship Diagram (ERD)](#entity-relationship-diagram-erd)  
10. [Environment Variables Summary](#environment-variables-summary)  
11. [License](#license)

---

## Overview

This **Learning Management System (LMS)** is built using **Spring Boot** (Java) in a **Monolithic** architecture.  
It showcases:
- **Role-based Access Control** (Admin, Instructor, Student, Donor)
- **Course & Enrollment Management**
- **Assessment & Submissions**
- **Email & Notification System**
- **Google OAuth2 & JWT Authentication**

Developed as part of the **SWER313 / SWER354** Spring 2025 coursework.

---

## Requirements

- **Java 21**  
- **Maven 3.8+**  
- **MySQL 8.x** (or a configured database)  
- **Postman** (for testing REST APIs)  
- Optional: **IntelliJ IDEA** (or any preferred IDE)

---

## Features and Modules

1. **User Module**  
   - Manage user registration, profile updates, login (JWT / Google OAuth2).  
   - Roles include: **Admin**, **Student**, **Instructor**, **Donor**.

2. **Course Module**  
   - Create, update, delete, and list courses.  
   - Assign instructors, handle course pricing (free or paid).

3. **Enrollment Module**  
   - Students can enroll/unenroll in courses.  
   - Track students’ courses, manage payments if the course is paid.

4. **Content Module**  
   - Instructors can upload lectures (PDF, videos, etc.).  
   - Students can view/download course materials.

5. **Assessment & Submission**  
   - Instructors create quizzes/assignments with correct answers.  
   - Students submit answers; possible auto-grading logic.

6. **Notification Module**  
   - Sends email notifications upon certain events (e.g., new assessments).  
   - In-app notifications, broadcast messages, scholarship updates, etc.

7. **Donations & Scholarships (Optional)**  
   - Donors can donate to scholarships or courses.  
   - Admin can create scholarships, students can apply, admin approves/rejects.

8. **Google OAuth2 Login**  
   - Users can opt to register/login using Google credentials.  
   - On success, an internal JWT is issued for subsequent requests.

---

## Project Structure

A simplified look at the **package structure**:

```
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┣ com.example.lms
 ┃ ┃ ┃ ┣ admin        # Admin logic & controllers
 ┃ ┃ ┃ ┣ assessment   # Assessment & Submissions
 ┃ ┃ ┃ ┣ attendance   # Attendance tracking (optional)
 ┃ ┃ ┃ ┣ auth         # Authentication (JWT, OAuth2)
 ┃ ┃ ┃ ┣ course       # Course, Content, Materials
 ┃ ┃ ┃ ┣ donor        # Donor module
 ┃ ┃ ┃ ┣ enrollment   # Enrollment logic
 ┃ ┃ ┃ ┣ notification # Email & in-app notifications
 ┃ ┃ ┃ ┣ student      # Student-specific logic
 ┃ ┃ ┃ ┣ user         # User entity & repository
 ┃ ┃ ┣ Application.java  # Main Spring Boot class
 ┃ ┣ resources
 ┃ ┃ ┣ application.properties  # Database, JWT, Mail, OAuth2 configs
 ┃ ┃ ┗ data.sql (optional)   # DB setup scripts if needed
 ┣ test
 ┃ ┣ java
 ┃ ┃ ┗ com.example.lms  # Unit & integration tests
```

---

## Installation & Setup

### 1. Database Setup

Make sure MySQL is running. Create a database, for example named `my_database`.  
Then update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/my_database?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=cnv5sn
spring.jpa.hibernate.ddl-auto=update
```
> **Note:** Adjust `username`, `password`, and DB name as needed.  
> The property `spring.jpa.hibernate.ddl-auto=update` is convenient for development (creates/updates tables automatically). In production, consider using `validate` or explicit migrations.

### 2. JWT Secret Key

```properties
jwt.secret=c2VjdXJlLXN0dWRlbnQtc2lnbi1rZXktMTIzNDU2Nzg=
```

### 3. Email SMTP Settings

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=hamsasulibe@gmail.com
spring.mail.password=kzhlsrsoskpedloh
```

### 4. Google OAuth2 Settings

```properties
spring.security.oauth2.client.registration.google.client-id=105348841322-qpabtvnp3qhi8nv3toh3cheord4okli8.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
```

---

## Running the Project

```bash
git clone https://github.com/S25-SWER313/project-step-1-hit.git
cd project-step-1-hit
mvn spring-boot:run
```

Visit [http://localhost:8080](http://localhost:8080)

---

## Authentication & Authorization

- **JWT Login** via `POST /auth/login`
- **Google OAuth2** via `/oauth2/authorization/google` → returns JWT

Use the JWT token in headers: `Authorization: Bearer <your-token>`

---

## API Testing with Postman

- Import: `HAYAT.postman_collection.json`
- Use environment variables for baseUrl and token
- Test all roles: Admin, Student, Instructor, Donor

---

## Entity Relationship Diagram (ERD)

See file: `docs/erd-diagram.png`

---

## Postman Collections

Use the following collections to test the API endpoints based on roles:


- [HAYAT](https://app.getpostman.com/join-team?invite_code=d08fb25466e8ef77ca39555a3638755b31158ce3f696e73deb5f574fd49ba480&target_code=b41db116738f3a61b59696542fe72544)


**Instructions:**  
Import these collections into Postman directly using the provided URLs to easily test each role-specific API.

---

## Environment Variables Summary

| Variable                                | Description                              |
|-----------------------------------------|------------------------------------------|
| `spring.datasource.url`                 | Database URL                             |
| `spring.datasource.username`            | DB username                              |
| `spring.datasource.password`            | DB password                              |
| `jwt.secret`                            | JWT signing key                          |
| `spring.mail.username`                  | SMTP email                               |
| `spring.mail.password`                  | SMTP password                            |
| `google.client-id`                      | Google OAuth2 Client ID                  |
| `google.client-secret`                  | Google OAuth2 Client Secret              |
| `google.redirect-uri`                   | Redirect URI                             |

---

## License

This project is for educational use as part of **Spring 2025 - SWER313 / SWER354** coursework.