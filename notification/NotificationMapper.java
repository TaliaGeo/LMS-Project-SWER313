package com.example.lms.notification;

import com.example.lms.exceptions.ResourceNotFoundException;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    private final UserRepository userRepository;

    public NotificationMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public NotificationDTO toDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setTimestamp(notification.getTimestamp());
        dto.setType(notification.getType());
        dto.setSent(notification.getSent()); // ✅ الجديد

        if (notification.getRecipient() != null) {
            dto.setUserId(notification.getRecipient().getId());
        }
        if (notification.getSender() != null) {
            dto.setSenderId(notification.getSender().getId());
        }
        return dto;
    }

    public Notification toEntity(NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setStatus(dto.getStatus());
        notification.setTimestamp(dto.getTimestamp());
        notification.setType(dto.getType());
        notification.setSent(dto.getSent() != null ? dto.getSent() : false); // ✅ الجديد

        if (dto.getUserId() != null) {
            User recipient = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with ID: " + dto.getUserId()));
            notification.setRecipient(recipient);
        }

        if (dto.getSenderId() != null) {
            User sender = userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sender not found with ID: " + dto.getSenderId()));
            notification.setSender(sender);
        }

        return notification;
    }

    public void updateEntity(Notification notification, NotificationDTO dto) {
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setStatus(dto.getStatus());
        notification.setType(dto.getType());
        notification.setSent(dto.getSent()); // ✅ الجديد

        if (dto.getUserId() != null) {
            User recipient = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with ID: " + dto.getUserId()));
            notification.setRecipient(recipient);
        }

        if (dto.getSenderId() != null) {
            User sender = userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sender not found with ID: " + dto.getSenderId()));
            notification.setSender(sender);
        }
    }
}
