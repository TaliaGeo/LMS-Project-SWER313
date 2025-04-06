package com.example.lms.notification;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO dto);
    NotificationDTO getNotificationById(Long id);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getNotificationsByUser(Long userId);
    void markAsRead(Long id);
    void deleteNotification(Long id);
    void sendNotificationToAllStudents(String title, String message, NotificationType type);

}
