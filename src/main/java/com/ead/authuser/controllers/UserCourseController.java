package com.ead.authuser.controllers;

import com.ead.authuser.clients.UserClient;
import com.ead.authuser.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Log4j2
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequiredArgsConstructor
public class UserCourseController {

    private final UserClient userClient;

    @GetMapping("users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.DESC) Pageable pageable,
                                                              @PathVariable UUID userId) {
        return ResponseEntity.status(OK).body(userClient.getAllCoursesByUser(userId,pageable));

    }
}
