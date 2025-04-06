package com.example.lms.notification;

import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(
            NotificationRepository notificationRepo,
            UserRepository userRepo,
            NotificationMapper mapper) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO dto) {
     
        Notification notification = mapper.toEntity(dto);

      
        if (notification.getTimestamp() == null) {
            notification.setTimestamp(LocalDateTime.now());
        }
       
        if (notification.getStatus() == null) {
            notification.setStatus("UNREAD");
        }

        Notification saved = notificationRepo.save(notification);
        return mapper.toDTO(saved);
    }

    @Override
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return mapper.toDTO(notification);
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        return notificationRepo.findByRecipientId(userId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        notification.setStatus("READ");
        notificationRepo.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }

    
    @Override
    public void sendNotificationToAllStudents(String title, String message, NotificationType type) {
       
        List<User> allStudents = userRepo.findAll()
                .stream()
                .filter(u -> "STUDENT".equalsIgnoreCase(u.getRole())
                          || "ROLE_STUDENT".equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());

        
        for (User student : allStudents) {
            Notification n = new Notification();
            n.setTitle(title);
            n.setMessage(message);
            n.setType(type);
            n.setTimestamp(LocalDateTime.now());
            n.setStatus("UNREAD");
            n.setRecipient(student);
            notificationRepo.save(n);
        }
    }
}