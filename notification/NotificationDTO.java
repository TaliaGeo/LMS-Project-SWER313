package com.example.lms.notification;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private String status;
    private LocalDateTime timestamp;
    private NotificationType type;
    private Long userId;     
    private Long senderId;   
    private Boolean sent;

}