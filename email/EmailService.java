package com.example.lms.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender,
            TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendWelcomeEmail(String to, String username) throws MessagingException {
    
        System.out.println("📨 Preparing to send welcome email to: " + to);
    
        Context context = new Context();
        context.setVariable("username", username);
    
        String htmlContent = templateEngine.process("welcome-email", context);
    
        sendEmail(to, "Welcome to Our Platform!", htmlContent);
    
        System.out.println("✅ Welcome email sent successfully to: " + to);
    }
    
    @Async
    public void sendAdminNotification(String to, String message) throws MessagingException {

        Context context = new Context();
        context.setVariable("message", message);

        String htmlContent = templateEngine.process("admin-notification", context);

        sendEmail(to, "New User Registered", htmlContent);
    }

    @Async
    public void sendFileUploadedEmail(String to, String username, String fileName) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("fileName", fileName);

        String htmlContent = templateEngine.process("file-uploaded", context);
        sendEmail(to, "File Uploaded Successfully", htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML
        mailSender.send(message);
    }
    @Async
public void sendCourseUpdateEmail(String to, String username, String courseName) throws MessagingException {
    Context context = new Context();
    context.setVariable("username", username);
    context.setVariable("courseName", courseName);
    String htmlContent = templateEngine.process("course-update", context);
    sendEmail(to, "Course Updated: " + courseName, htmlContent);
}
@Async
public void sendAssignmentDueEmail(String to, String username, String courseName) throws MessagingException {
    Context context = new Context();
    context.setVariable("username", username);
    context.setVariable("courseName", courseName);

    String htmlContent = templateEngine.process("assignment-due", context);
    sendEmail(to, "New Assignment Due in " + courseName, htmlContent);
}

}
