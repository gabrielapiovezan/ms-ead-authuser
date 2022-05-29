package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()) {
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getUser(user.getUserId())).withSelfRel());
            }
        }

        return ResponseEntity.status(OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    ResponseEntity<Object> getUser(@PathVariable UUID userId) {
        Optional<UserModel> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.status(OK).body(user.get());
        }
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
        log.debug("DELETE updateUser userDTO saved {}", userId);
        log.info("DELETE user deleted with successfully {}", userId);
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            userService.deleteUser(user.get());
            return ResponseEntity.status(OK).body("User deleted with success");
        }
    }

    @PutMapping("/{userId}")
    ResponseEntity<Object> updateUser(@PathVariable UUID userId,
                                      @RequestBody @Validated(UserDTO.UserView.UserPut.class)
                                      @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        log.debug("PUT updateUser userDTO received {}", userDTO.toString());
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            var userModel = user.get();
            userModel.setFullName(userDTO.getFullName());
            userModel.setPhoneNumber(userDTO.getPhoneNumber());
            userModel.setCpf(userDTO.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updateUser(userModel);
            log.debug("PUT updateUser userDTO saved {}", userDTO);
            log.info("user saved successfully userId {}", userModel.getUserId());
            return ResponseEntity.status(OK).body(userModel);
        }
    }

    @PutMapping("/{userId}/password")
    ResponseEntity<Object> updatePassWord(@PathVariable UUID userId,
                                          @RequestBody @Validated(UserDTO.UserView.PassWordPut.class)
                                          @JsonView(UserDTO.UserView.PassWordPut.class) UserDTO userDTO) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            log.warn("Missatched older password {}", userDTO.getUserName());
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        if (!user.get().getPassword().equals(userDTO.getOldPassword())) {
            return ResponseEntity.status(CONFLICT).body("Error: Mismatched oldPassWord");
        } else {
            var userModel = userService.update(user.get());
            userModel.setPassword(userDTO.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updatePassword(userModel);
            return ResponseEntity.status(OK).body("PassWord updated successfull. ");
        }
    }

    @PutMapping("/{userId}/image")
    ResponseEntity<Object> updateImage(@PathVariable UUID userId,
                                       @RequestBody @Validated(UserDTO.UserView.ImagePut.class)
                                       @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        } else {
            var userModel = userService.update(user.get());
            userModel.setImageUrl(userDTO.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updateUser(userModel);
            return ResponseEntity.status(OK).body(userModel);
        }
    }


}
