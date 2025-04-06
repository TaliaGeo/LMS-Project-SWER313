package com.example.lms.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/notifications")
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO dto) {
        System.out.println("🔔 createNotification called by ADMIN");
        return ResponseEntity.ok(notificationService.createNotification(dto));
    }
    
   
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT', 'DONOR')")
    @GetMapping("/api/notifications/id/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT', 'DONOR')")
    @GetMapping("/api/notifications/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT', 'DONOR')")
    @PutMapping("/api/notifications/{id}/mark-as-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT', 'DONOR')")
    @DeleteMapping("/api/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
