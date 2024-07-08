package org.app.instagram_be.controller;

import org.app.instagram_be.model.entities.User;
import org.app.instagram_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userInput}")
    public ResponseEntity<?> getUser(@PathVariable String userInput) {
        Optional<User> user = userService.findByEmailOrPhoneNumber(userInput);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }
}
