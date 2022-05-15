package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserCourseController {

    private final CourseClient courseClient;

    private final UserService userService;

    private final UserCourseService userCourseService;

    @GetMapping("users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.DESC) Pageable pageable,
                                                               @PathVariable UUID userId) {
        return ResponseEntity.status(OK).body(courseClient.getAllCoursesByUser(userId, pageable));

    }

    @PostMapping("users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID userId,
                                                               @RequestBody UserCourseDTO userCourseDTO) {

        var optUserModer = userService.findById(userId);

        if (optUserModer.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("User Not Found");
        }

        if (userCourseService.existByUserAndCourseId(optUserModer.get(), userCourseDTO.getCourseId())) {
            return ResponseEntity.status(CONFLICT).body("Error:Subscription already exists");
        }
        UserCourseModel userCourseModel = userCourseService.save(optUserModer.get().convertToUserCourseModel(userCourseDTO.getCourseId()));
        return ResponseEntity.status(CREATED).body(userCourseModel);

    }
}

