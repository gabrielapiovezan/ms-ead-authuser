package com.ead.authuser.controllers;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

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
    ResponseEntity<Object> updateUser(@PathVariable UUID userId, @RequestBody @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            var userModel = userService.update(user.get());
            userModel.setFullName(userDTO.getFullName());
            userModel.setPhoneNumber(userDTO.getPhoneNumber());
            userModel.setCpf(userDTO.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(OK).body(userModel);
        }
    }

    @PutMapping("/{userId}/password")
    ResponseEntity<Object> updatePassWord(@PathVariable UUID userId, @RequestBody @JsonView(UserDTO.UserView.PassWordPut.class) UserDTO userDTO) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        if (!user.get().getPassword().equals(userDTO.getOldPassword())) {
            return ResponseEntity.status(CONFLICT).body("Error: Mismatched oldPassWord");
        } else {
            var userModel = userService.update(user.get());
            userModel.setPassword(userDTO.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(OK).body("PassWord updated successfull. ");
        }
    }

    @PutMapping("/{userId}/image")
    ResponseEntity<Object> updateImage(@PathVariable UUID userId, @RequestBody @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            var userModel = userService.update(user.get());
            userModel.setImageUrl(userDTO.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(OK).body(userModel);
        }
    }


}
