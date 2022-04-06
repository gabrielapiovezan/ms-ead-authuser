package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    ResponseEntity<Object> getUser(@PathVariable UUID userId) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.status(OK).body(user.get());
        }
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            userService.delete(user.get());
            return ResponseEntity.status(OK).body("User deleted with success");
        }
    }

    @PutMapping("/{userId}")
    ResponseEntity<Object> updateUser(@PathVariable UUID userId) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            userService.update(user.get());
            return ResponseEntity.status(OK).body("User deleted with success");
        }
    }


}
