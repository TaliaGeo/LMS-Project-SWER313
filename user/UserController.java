package com.example.lms.user;

import org.springframework.beans.factory.annotation.Value;
import com.example.lms.email.EmailService;
import com.example.lms.user.UserDto;
import com.example.lms.user.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Value("${admin.email}")
    private String adminEmail;

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(@RequestBody UserDto userDto) {

        UserDto createdUser = userService.createUser(userDto);

        try {
            emailService.sendWelcomeEmail(createdUser.getEmail(), createdUser.getName());

            emailService.sendAdminNotification(adminEmail, "New user registered: " + createdUser.getName());

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

        EntityModel<UserDto> model = EntityModel.of(createdUser);
        model.add(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));

        return ResponseEntity.ok(model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EntityModel<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        List<EntityModel<UserDto>> models = users.stream()
                .map(user -> EntityModel.of(user)
                        .add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel())
                        .add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        EntityModel<UserDto> model = EntityModel.of(updatedUser);
        model.add(linkTo(methodOn(UserController.class).getUserById(updatedUser.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        EntityModel<UserDto> model = EntityModel.of(user);
        model.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(model);
    }
}
